package com.tolmic.pseudorandom;

/**
 *  Sensor № 6
 * 
 * @author Dmitry Tolmachev
 */
public class PseudorandomNumbers {

    private double r;

    private static double C = 5;

    public PseudorandomNumbers(double seed) {
        this.r = seed;
    }

    public PseudorandomNumbers(double seed, double C1) {
        this.r = seed;
        C = C1;
    }

    public static void setC(double C1) {
        C = C1;
    }

    public static double getC() {
        return C;
    }

    private double frac(double number) {
        return number - (int) number;
    }

    public double getCurr() {
        return r;
    }
    
    // alternative 97
    public double run() {
        r = frac(C * r + Math.PI);

        return r;
    }

}
