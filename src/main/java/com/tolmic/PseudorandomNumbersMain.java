package com.tolmic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import org.apache.poi.hpsf.Array;

import static com.tolmic.utils.MathUtils.countHiSqTable;

public class PseudorandomNumbersMain {

    private static final double SEED = 1.0;
    private static final int K = 90000000;
    private static final double ALPHA = 0.95;
    private static final double CUBE_EDGE_SIZE = 1.0;

    // seed = 23, i = 20354777
    // seed = 12, i = 13292787
    // seed = 1.0, i = 11405875
    // seed = 0.5, i = 9809936
    // seed = 0.0, i = 32358049
    // seed = 50, i = 21717434
    // seed = 80, i = 21717434
    // seed = 40, i = 20354777
    private static int findL(double seed, int period1) {
        PseudorandomNumbers pn = new PseudorandomNumbers(seed);
        PseudorandomNumbers pn1 = new PseudorandomNumbers(seed);
        
        double r = seed;
        double r1 = seed;
        for (int i = 1; true; i++) {
            r = pn.run();

            if (i > period1) {
                r1 = pn1.run();

                if (r == r1) {
                    return i;
                }
            }
        }
    }

    private static int findPeriod1(double seed) {
        PseudorandomNumbers pn = new PseudorandomNumbers(seed);

        double ril = -1;
        for (int i = 1; true; i++) {
            double ri = pn.run();

            if (i == K) {
                ril = ri;
            } else if (i > K && ri == ril) {
                return i - K;
            }
        }
    }

    private static void test11() {
        System.out.println("Test 1.1:");

        for (double num : List.of(1.0, 2.0, 2.5, 23.0, 90.0, 92.0)) {
            int period1 = findPeriod1(num);

            System.out.println("seed = " + num + ":");
            System.out.println("1: " + period1);
            System.out.println("L: " + findL(num, period1));
        }
    }

    private static double[] getPoint(PseudorandomNumbers pn, int d) {
        double[] p = new double[d];

        for (int i = 0; i < d; i++) {
            p[i] = pn.run();
        }

        return p;
    }

    private static int getMiniCubeNumber(double[] p, int q) {
        int number = 0;

        double m = CUBE_EDGE_SIZE / (double) q;
        int n = p.length;
        for (int i = 0, d = 1; i < n; i++, d *= q) {
            int index = (int) (p[i] / m);
            
            number += index * d;
        }

        return number;
    }

    private static double countHiSq(int[] m, double d) {
        double hiSq = 0;

        int M = m.length;
        for (int i = 0; i < M; i++) {
            hiSq += Math.pow((double) m[i] - d, 2);
        }

        return hiSq / d;
    }

    private static void countMiniCubeDestribution(int[] m, List<double[]> points, int q) {
        int n = points.size();

        for (int i = 0; i < n; i++) {
            int mi = getMiniCubeNumber(points.get(i), q);

            m[mi] = m[mi] + 1;
        }
    }

    private static List<double[]> findPoints(double seed, int N, int k) {
        PseudorandomNumbers pn = new PseudorandomNumbers(seed);
        List<double[]> points = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            points.add(getPoint(pn, k));
        }

        return points;
    }

    private static void gistogramTests(double seed, int k) {
        int q = 5;
        int N = findL(seed, findPeriod1(seed)) / 2;

        int M = (int) Math.pow(q, k);
        int[] m = new int[M];

        List<double[]> points = findPoints(seed, N, k);
        countMiniCubeDestribution(m, points, q);

        double hiSq = countHiSq(m, (double) N / (double) M);

        double hiSqTable = countHiSqTable(ALPHA, M - 2);

        System.out.println(String.format("Хи расчётный (%s) %s Хи табличного (%s)", hiSq, (hiSq > hiSqTable ? ">" : "<"), hiSqTable));
    }

    private static void test12(double seed) {
        System.out.println("Test 1.2:");

        gistogramTests(seed, 5);
    }

    private static void test21(double seed) {
        System.out.println("Test 2.1:");

        gistogramTests(seed, 1);
    
        // graphic
    }

    private static void test22(double seed) {
        System.out.println("Test 2.2:");

        gistogramTests(seed, 2);
    }

    private static void test23(double seed) {
        System.out.println("Test 2.3:");

        gistogramTests(seed, 3);
    }

    private static void test24(double seed) {
        System.out.println("Test 2.2:");

        gistogramTests(seed, 4);
    }

    private static void test3(double seed) {
        System.out.println("Test 3:");
    }

    private static void test5(double seed) {
        System.out.println("Test 5:");

        PseudorandomNumbers pn = new PseudorandomNumbers(seed);
        int k = 3;
        int n = 100;

        double[] c = new double[n];

        for (int i = 0; i < n; i++) {
            if (i > 0) {
                pn.run();
            }

            PseudorandomNumbers pnik = new PseudorandomNumbers(pn.getCurr());
            for (int j = 0; j < k; j++) {
                pnik.run();
            }

            double ri = pn.getCurr();
            double rik = pnik.getCurr();

            
        }
    }

    public static void main(String[] args) {
        test11();

        // test23(0);

        // test21(1.0);
    }
}
