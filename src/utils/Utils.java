package utils;

import java.util.Random;

public class Utils {
    public static double atanh(double x) {
        return (Math.log(1 + x) - Math.log(1 - x)) / 2;
    }
}
