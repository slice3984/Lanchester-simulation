import utils.Clock;

public abstract class SimulationLogic {
    protected int populationRed;
    protected int populationBlue;
    protected final double effectivenessRed;
    protected final double effectivenessBlue;
    protected double leftFractionalShotsRed;
    protected double leftFractionalShotsBlue;
    protected final double timescale;
    protected final Clock clock;

    SimulationLogic(int populationRed, int populationBlue, double effectivenessRed, double effectivenessBlue, double timescale) {
        this.populationRed = populationRed;
        this.populationBlue = populationBlue;
        this.effectivenessRed = effectivenessRed;
        this.effectivenessBlue = effectivenessBlue;
        this.timescale = timescale;
        leftFractionalShotsRed = 0;
        leftFractionalShotsBlue = 0;
        clock = new Clock();
        clock.start(); // start the clock when class is instantiated
    }

    public abstract PopulationChangeData simulationStep();
}
