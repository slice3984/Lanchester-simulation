public class LanchesterEvaluationSimulation extends SimulationLogic {
    private int currPopulationRed;
    private int currPopulationBlue;

    LanchesterEvaluationSimulation(int populationRed, int populationBlue, double effectivenessRed, double effectivenessBlue, double timescale) {
        super(populationRed, populationBlue, effectivenessRed, effectivenessBlue, timescale);
        System.out.println(timescale);
        currPopulationRed = populationRed;
        currPopulationBlue = populationBlue;
    }

    @Override
    public PopulationChangeData simulationStep() {
        // Not a real simulation, evaluates the Lanchester functions for population change
        double currTime = clock.getDeltaMillis() * timescale;


        int populationRed = (int) Lanchester.calculatePopulation(this.populationRed, this.populationBlue, effectivenessRed, effectivenessBlue, currTime);
        int populationBlue = (int) Lanchester.calculatePopulation(this.populationBlue, this.populationRed, effectivenessBlue, effectivenessRed, currTime);

        PopulationChangeData populationChangeData = new PopulationChangeData(
                populationRed,
                populationBlue,
                currPopulationBlue - populationBlue,
                currPopulationRed - populationRed
        );

        currPopulationRed = populationRed;
        currPopulationBlue = populationBlue;

        return populationChangeData;
    }
}
