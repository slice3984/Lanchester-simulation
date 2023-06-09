import utils.Clock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationWindowJFrame implements ActionListener {
    private final Timer timer;
    private SimulationPanel simulationPanelLanchesterEvaluated;
    private SimulationPanel simulationPanelReal;
    private SimulationGraphPanel simulationGraphPanel;
    private  InfoPanel infoPanel;
    private boolean firstSimulationIteration = true;
    private int populationRed;
    private int populationBlue;
    private  double effectivenessRed;
    private double effectivenessBlue;
    private int lanchesterRed = 0;
    private int lanchesterBlue = 0;
    private Clock clock;
    private double time;
    private boolean updateLanchesterRedGraph = true;
    private boolean updateLanchesterBlueGraph = true;


    SimulationWindowJFrame(int fps) {
        timer = new Timer(1000 / fps, this);
        clock = new Clock();
    }

    public void startSimulation(int populationRed, int populationBlue, double efficiencyRed, double efficiencyBlue, double simTime, double timescale) {
        this.populationRed = populationRed;
        this.populationBlue = populationBlue;
        this.effectivenessRed = efficiencyRed;
        this.effectivenessBlue = efficiencyBlue;

        // Lanchester evaluation simulation
        LanchesterEvaluationSimulation lanchesterEvaluationSimulation = new LanchesterEvaluationSimulation(populationRed, populationBlue, efficiencyRed, efficiencyBlue, timescale);
        simulationPanelLanchesterEvaluated = new SimulationPanel(populationRed, populationBlue, efficiencyRed, efficiencyBlue, lanchesterEvaluationSimulation);

        // Real simulation
        RealSimulation realSimulation = new RealSimulation(populationRed, populationBlue, efficiencyRed, efficiencyBlue, timescale);
        simulationPanelReal = new SimulationPanel(populationRed, populationBlue, efficiencyRed, efficiencyBlue, realSimulation);


        simulationGraphPanel = new SimulationGraphPanel(Math.max(populationBlue, populationRed), (int) Math.ceil(simTime / 1000));
        infoPanel = new InfoPanel(
                efficiencyRed,
                efficiencyBlue,
                populationRed,
                populationBlue,
                (int) Lanchester.calculateGEvaluated(populationRed, populationBlue, efficiencyRed, efficiencyBlue),
                (int) Lanchester.calculateHEvaluated(populationRed, populationBlue, efficiencyRed, efficiencyBlue),
                timescale,
                0);

        JFrame frameGraph = new JFrame();
        frameGraph.setSize(600, 600);
        frameGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameGraph.setLocationRelativeTo(null);
        frameGraph.setLayout(null);

        new SimulationJFrame(simulationPanelLanchesterEvaluated, simulationPanelReal, simulationGraphPanel, infoPanel, 60);
        time = System.currentTimeMillis();
        clock.start();
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double newTime = System.currentTimeMillis();
        double deltaTime = newTime - time;

        if (!simulationPanelReal.isSimulationDone()) {
            simulationPanelLanchesterEvaluated.update();
            simulationPanelReal.update();

            // Lanchester - Only update graph if population changed
            if (simulationPanelLanchesterEvaluated.getCurrPopulationBlue() > 0 && simulationPanelLanchesterEvaluated.getCurrPopulationRed() > 0) {
                simulationGraphPanel.update(clock.getDeltaMillis() / 1000, simulationPanelLanchesterEvaluated.getCurrPopulationBlue(), new Color(0, 0, 200));
                simulationGraphPanel.update(clock.getDeltaMillis() / 1000, simulationPanelLanchesterEvaluated.getCurrPopulationRed(), new Color(200, 0, 0));
            }

            // Real
            simulationGraphPanel.update(clock.getDeltaMillis() / 1000, simulationPanelReal.getCurrPopulationBlue(), new Color(0, 0, 200));
            simulationGraphPanel.update(clock.getDeltaMillis() / 1000, simulationPanelReal.getCurrPopulationRed(), new Color(200, 0, 0));

            infoPanel.update(
                    simulationPanelReal.getCurrPopulationRed(),
                    simulationPanelReal.getCurrPopulationBlue(),
                    simulationPanelLanchesterEvaluated.getCurrPopulationRed(),
                    simulationPanelLanchesterEvaluated.getCurrPopulationBlue(),
                    clock.getDeltaMillis()
            );
        } else {
            timer.stop();
        }

        time = newTime;
    }
}
