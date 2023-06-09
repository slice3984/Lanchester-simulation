import javax.swing.*;
import java.awt.*;

public class SimulationViewSwitcher extends JPanel {
    private final JLabel label;
    private final JButton button;
    private boolean isDisplayingLanchesterEvaluated = true;

    public SimulationViewSwitcher(SimulationJFrame simulationJFrame) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        label = new JLabel("Current Visualization: Lanchester Evaluated");
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(18f));

        button = new JButton("Switch View");
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFont(button.getFont().deriveFont(20f));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setPreferredSize(new Dimension(button.getPreferredSize().width * 2, button.getPreferredSize().height)); // Double the button width

        add(label, BorderLayout.WEST);
        add(button, BorderLayout.EAST);

        setBounds(0, 0, Constants.VS_WIDTH, Constants.VS_HEIGHT);

        button.addActionListener(e -> {
            if (isDisplayingLanchesterEvaluated) {
                label.setText("Current Visualization: Real Simulation");
                isDisplayingLanchesterEvaluated = false;
            } else {
                label.setText("Current Visualization: Lanchester Evaluated");
                isDisplayingLanchesterEvaluated = true;
            }

            simulationJFrame.switchSimulationView();
        });
    }
}
