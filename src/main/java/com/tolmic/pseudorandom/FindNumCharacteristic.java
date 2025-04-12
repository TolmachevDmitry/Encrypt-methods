package com.tolmic.pseudorandom;

import java.util.HashMap;

public final class FindNumCharacteristic {
    private static final int K = 12000000;

    // seed = 23, i = 20354777
    // seed = 12, i = 13292787
    // seed = 1.0, i = 11405875
    // seed = 0.5, i = 9809936
    // seed = 0.0, i = 32358049
    // seed = 50, i = 21717434
    // seed = 80, i = 21717434
    // seed = 40, i = 20354777
    public static int findL(int period1, double seed) {
        PseudorandomNumbers pn = new PseudorandomNumbers(seed);
        PseudorandomNumbers pn1 = new PseudorandomNumbers(seed);
        
        double r = pn.getCurr();
        double r1 = pn1.getCurr();
        for (int i = 1; period1 > 0; i++) {
            r = pn.run();

            if (i > period1) {
                r1 = pn1.run();

                if (r == r1) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static int findPeriod1ByBruteForce(double seed) {
        PseudorandomNumbers pn = new PseudorandomNumbers(seed);

        HashMap<Double, Integer> valueAndIndex = new HashMap<>();

        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            double value = pn.run();

            Integer indexFirstMeet = valueAndIndex.get(value);

            if (indexFirstMeet != null) {
                return i - indexFirstMeet;
            } else {
                valueAndIndex.put(value, i);
            }
        }

        return -1;
    }

    public static int findPeriod1(double seed) {
        PseudorandomNumbers pn = new PseudorandomNumbers(seed);

        double ril = -1;
        for (int i = 1; i > 0; i++) {
            double ri = pn.run();

            if (i == K) {
                ril = ri;
            } else if (i > K && ri == ril) {
                return i - K;
            }
        }

        return -1;
    }

}
