import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationJFrame extends JFrame implements ActionListener {
    private final SimulationPanel simulationPanelLanchesterEvaluated;
    private final SimulationPanel simulationPanelReal;
    private final SimulationGraphPanel simulationGraphPanel;
    private final InfoPanel infoPanel;
    private final Timer timer;
    private final int updateTime;
    private boolean isDisplayingLanchesterEvaluated;


    SimulationJFrame(SimulationPanel simulationPanelLanchesterEvaluated, SimulationPanel simulationPanelReal, SimulationGraphPanel simulationGraphPanel, InfoPanel infoPanel, int fps) {
        super("Lanchester Square Law Simulation");
        this.simulationPanelLanchesterEvaluated = simulationPanelLanchesterEvaluated;
        this.simulationPanelReal = simulationPanelReal;
        this.simulationGraphPanel = simulationGraphPanel;
        this.infoPanel = infoPanel;
        isDisplayingLanchesterEvaluated = true;
        
       setBounds(0, 0, Constants.WIDTH, Constants.HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(Constants.BG_COLOR);

        updateTime = 1000 / fps;
        timer = new Timer(updateTime, this);

        infoPanel.setLocation(0, 0);
        add(infoPanel);

        simulationGraphPanel.setLocation(Constants.WIDTH - Constants.SGP_WIDTH - 30, 20);
        add(simulationGraphPanel);

        SimulationViewSwitcher simulationViewSwitcher = new SimulationViewSwitcher(this);
        simulationViewSwitcher.setLocation(0, Constants.IP_HEIGHT);
        add(simulationViewSwitcher);

        simulationPanelLanchesterEvaluated.setLocation(0, Constants.IP_HEIGHT + 20 );
        add(simulationPanelLanchesterEvaluated);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        timer.start();
    }

    protected void switchSimulationView() {
        if (isDisplayingLanchesterEvaluated) {
            remove(simulationPanelLanchesterEvaluated);
            simulationPanelReal.setLocation(0, Constants.IP_HEIGHT + 20);
            add(simulationPanelReal);
            isDisplayingLanchesterEvaluated = false;
        } else {
            remove(simulationPanelReal);
            simulationPanelLanchesterEvaluated.setLocation(0, Constants.IP_HEIGHT + 20);
            add(simulationPanelLanchesterEvaluated);
            isDisplayingLanchesterEvaluated = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();

        simulationPanelLanchesterEvaluated.repaint();
        // simulationPanel.getTopLevelAncestor().repaint();

        simulationGraphPanel.repaint();
        // simulationGraphPanel.getTopLevelAncestor().repaint();
    }
}