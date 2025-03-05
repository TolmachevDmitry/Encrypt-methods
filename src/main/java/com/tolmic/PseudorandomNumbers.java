package com.tolmic;

/**
 *  Sensor № 6
 * 
 * @author Dmitry Tolmachev
 */
public final class PseudorandomNumbers {

    private static double r;

    public static void seed(double r0) {
        PseudorandomNumbers.r = r0;
    }

    private static double frac(double number) {
        return number - (int) number;
    }
    
    public static double solve() {
        r = frac(97 * r + Math.PI);

        return r;
    }

}
