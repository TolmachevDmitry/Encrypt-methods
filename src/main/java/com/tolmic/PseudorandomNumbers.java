package com.tolmic;

/**
 *  Sensor № 6
 * 
 * @author Dmitry Tolmachev
 */
public class PseudorandomNumbers {

    private double r;

    public PseudorandomNumbers(double seed) {
        this.r = seed;
    }

    private double frac(double number) {
        return number - (int) number;
    }

    public double getCurr() {
        return r;
    }
    
    // alternative 97
    public double run() {
        r = frac(10000 * r + Math.PI);

        return r;
    }

}
