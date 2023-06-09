import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ConfigurationJFrame extends JFrame {
    private int populationRed;
    private int populationBlue;
    private double effectivenessRed;
    private double effectivenessBlue;
    private double timescale;
    private final JLabel simTimeLabel;
    private double simTime;
    private final JLabel timescaleLabel;

    ConfigurationJFrame() {
        super("Lanchester Square Law Simulation - Configuration");
        setLocationRelativeTo(null);
        setResizable(false);
        GridLayout gridLayout = new GridLayout(6, 2, 5, 10);
        setLayout(gridLayout);

        populationRed = -1;
        populationBlue = -1;
        effectivenessRed = -1;
        effectivenessBlue = -1;
        timescale = 0.000010;
        simTime = 0;

        simTimeLabel = new JLabel("Estimated Simulation Time: Missing values");

        JPanel[] inputsRed = generateInputs(Team.RED);
        JPanel[] inputsBlue = generateInputs(Team.BLUE);

        JPanel populationsPanel = new JPanel(new FlowLayout());
        populationsPanel.add(inputsRed[0]);
        populationsPanel.add(inputsBlue[0]);
        add(populationsPanel);

        JPanel effectivenessPanel = new JPanel(new FlowLayout());
        effectivenessPanel.add(inputsRed[1]);
        effectivenessPanel.add(inputsBlue[1]);
        add(effectivenessPanel);

        JPanel timescalePanel = new JPanel(new FlowLayout());
        timescaleLabel = new JLabel("Timescale");

        JSpinner timescaleSpinner = new JSpinner(new SpinnerNumberModel(0.000010, 0.000001, 1.0, 0.000010));
        timescaleSpinner.setEditor(new JSpinner.NumberEditor(timescaleSpinner, "0.000000"));
        timescaleSpinner.addChangeListener(e -> {
            timescale = (double) timescaleSpinner.getValue();

            if (areValuesSet()) {
                simTime = Lanchester.calculateSimulationTimeLanchester(populationRed, populationBlue, effectivenessBlue, effectivenessRed);
                simTimeLabel.setText("Estimated Simulation Time: " + String.format("%.3f", simTime / timescale / 1000) + "s");
            }
        });

        timescalePanel.add(timescaleLabel);
        timescalePanel.add(timescaleSpinner);

        add(timescalePanel);
        add(new JLabel()); // Empty label to occupy the first column

        add(simTimeLabel);

        JButton simStartBtn = new JButton("Start Simulation");
        simStartBtn.addActionListener(e -> {
            if (!areValuesSet()) {
                JOptionPane.showMessageDialog(this, "Values are missing", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                setVisible(false);
                // The simulation takes about 1.5 times the estimated time to complete
                double timeUsedForGraph = 1.5 * (simTime / timescale);
                Main.startSimulation(populationRed, effectivenessRed, populationBlue, effectivenessBlue, timeUsedForGraph, timescale);
                dispose();
            }
        });

        add(simStartBtn, BorderLayout.CENTER); // Button spans across the entire row

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel[] generateInputs(Team team) {
        NumberFormat longFormat = NumberFormat.getIntegerInstance();
        NumberFormatter longNumberFormatter = new NumberFormatter(longFormat);
        longNumberFormatter.setValueClass(Integer.class);
        longNumberFormatter.setAllowsInvalid(false);
        longNumberFormatter.setMinimum(0);
        longNumberFormatter.setMaximum(10000);

        NumberFormat doubleFormat = DecimalFormat.getInstance();
        doubleFormat.setMinimumIntegerDigits(5);
        doubleFormat.setMaximumFractionDigits(6);
        NumberFormatter doubleNumberFormatter = new NumberFormatter(doubleFormat);
        doubleNumberFormatter.setMinimum(0.000001);

        JPanel[] fields = new JPanel[2];

        // Label + Input for red and blue populations
        String[] labels = new String[]{"Population " + (team == Team.RED ? "Red (G)" : "Blue (H)"),
                "Effectiveness " + (team == Team.RED ? "Red (r)" : "Blue (s)")};

        for (int i = 0; i < 2; i++) {
            JLabel label = new JLabel(labels[i]);

            JFormattedTextField in = new JFormattedTextField();
            if (i == 0) {
                in.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
                    @Override
                    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                        NumberFormat format = NumberFormat.getIntegerInstance();
                        NumberFormatter intNumberFormatter = new NumberFormatter(format);
                        intNumberFormatter.setValueClass(Integer.class);
                        intNumberFormatter.setAllowsInvalid(false);
                        intNumberFormatter.setMinimum(0);
                        intNumberFormatter.setMaximum(10000);

                        return intNumberFormatter;
                    }
                });
            } else {
                in.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
                    @Override
                    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                        NumberFormat format = DecimalFormat.getInstance();
                        format.setMinimumFractionDigits(6);
                        format.setMaximumFractionDigits(6);
                        format.setRoundingMode(RoundingMode.HALF_UP);
                        InternationalFormatter formatter = new InternationalFormatter(format);
                        formatter.setAllowsInvalid(false);
                        return formatter;
                    }
                });
            }

            in.setColumns(6);

            int finalI = i;
            in.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    String content = in.getText();

                    if (content.length() == 0) {
                        return;
                    }

                    // Population - int
                    if (finalI == 0) {
                        int population = Integer.parseInt(content.replaceAll("\\D+", ""));

                        if (team == Team.RED) {
                            populationRed = population;
                        } else {
                            populationBlue = population;
                        }
                    } else {
                        // Efficiency - decimal
                        double efficiency = Double.parseDouble(content.replace(",", "."));

                        if (team == Team.RED) {
                            effectivenessRed = efficiency;
                        } else {
                            effectivenessBlue = efficiency;
                        }
                    }

                    if (areValuesSet()) {
                        simTime = Lanchester.calculateSimulationTimeLanchester(populationRed, populationBlue, effectivenessBlue, effectivenessRed);
                        simTimeLabel.setText("Estimated Simulation Time: " + String.format("%.3f", simTime / timescale / 1000) + "s");
                    }
                }

            });

            JPanel panel = new JPanel(new FlowLayout());
            panel.add(label);
            panel.add(in);

            fields[i] = panel;
        }

        return fields;
    }

    private boolean areValuesSet() {
        return populationRed > 0 && populationBlue > 0 && effectivenessRed > 0 && effectivenessBlue > 0;
    }
}
