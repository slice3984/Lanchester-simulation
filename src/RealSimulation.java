public class RealSimulation extends SimulationLogic {
    RealSimulation(int populationRed, int populationBlue, double effectivenessRed, double effectivenessBlue, double timescale) {
        super(populationRed, populationBlue, effectivenessRed, effectivenessBlue, timescale);
    }

    @Override
    public PopulationChangeData simulationStep() {
        // We are only dealing with time deltas for the real simulation
        double deltaTime = clock.getDeltaMillis();

        double totalEliminationsRed = leftFractionalShotsRed + (populationRed * deltaTime * effectivenessRed) * timescale;
        int eliminationsThisFrameRed = (int) Math.floor(totalEliminationsRed);
        leftFractionalShotsRed = totalEliminationsRed - eliminationsThisFrameRed;

        double totalEliminationsBlue = leftFractionalShotsBlue + (populationBlue * deltaTime * effectivenessBlue) * timescale;
        int eliminationsThisFrameBlue = (int) Math.floor(totalEliminationsBlue);
        leftFractionalShotsBlue = totalEliminationsBlue - eliminationsThisFrameBlue;

        populationRed -= eliminationsThisFrameRed;
        populationBlue -= eliminationsThisFrameBlue;

        // Take a new snapshot of the clock for the next frame
        clock.takeSnapshot();

        return new PopulationChangeData(
                populationRed,
                populationBlue,
                eliminationsThisFrameRed,
                eliminationsThisFrameBlue
        );
    }
}
