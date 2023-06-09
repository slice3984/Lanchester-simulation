import utils.Utils;

public class Lanchester {
    public static double calculatePopulation(int startingAmountFirstPopulation, int startingAmountSecondPopulation, double s, double r, double t) {
        double x = Math.sqrt(s * r) * t;

        return startingAmountFirstPopulation * Math.cosh(x) - Math.sqrt(r/s) * startingAmountSecondPopulation * Math.sinh(x);
    }

    public static double calculateSimulationTimeLanchester(int startingAmountFirstPopulation, int startingAmountSecondPopulation, double s, double r) {
        double l = calculateL(startingAmountFirstPopulation, startingAmountSecondPopulation, s, r);

        // Second population won (H)
        if (l < 0) {
            System.out.println("BLUE WON");
            return Utils.atanh(((double) startingAmountFirstPopulation / startingAmountSecondPopulation) * Math.sqrt(s/r) ) / Math.sqrt(s * r);
        } else {
            // First population won (G)
            System.out.println("RED WON");
            return Utils.atanh((((double) startingAmountSecondPopulation / startingAmountFirstPopulation) * Math.sqrt(r/s)) ) / Math.sqrt(s * r);
        }
    }

    public static double calculateL(int startingAmountFirstPopulation, int startingAmountSecondPopulation, double s, double r) {
        return s * Math.pow(startingAmountFirstPopulation, 2) - r * Math.pow(startingAmountSecondPopulation, 2);
    }

    public static double calculateHEvaluated(int startingAmountFirstPopulation, int startingAmountSecondPopulation,double s, double r) {
        double l = calculateL(startingAmountFirstPopulation, startingAmountSecondPopulation, s, r);

        if (l < 0) {
            return Math.sqrt(-l / r);
        } else {
            return 0;
        }
    }

    public static double calculateGEvaluated(int startingAmountFirstPopulation, int startingAmountSecondPopulation, double s, double r) {
        double l = calculateL(startingAmountFirstPopulation, startingAmountSecondPopulation, s, r);

        if (l > 0) {
            return Math.sqrt(l / s);
        } else {
            return 0;
        }
    }
}
