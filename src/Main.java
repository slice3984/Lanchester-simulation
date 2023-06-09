public class Main {
    public static void main(String[] args) {
        new ConfigurationJFrame();
    }

    public static void startSimulation(int populationRed, double efficiencyRed, int populationBlue, double efficiencyBlue, double simTime, double timescale) {
            SimulationWindowJFrame simulationLogic = new SimulationWindowJFrame(60);
        simulationLogic.startSimulation(populationRed, populationBlue, efficiencyRed, efficiencyBlue, simTime, timescale);
    }
}
