package com.tolmic.pseudorandom;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.jfree.data.statistics.HistogramDataset;

import static com.tolmic.utils.MathUtils.countHiSqTable;
import static com.tolmic.pseudorandom.PlotBuilder.*;
import static com.tolmic.pseudorandom.FindNumCharacteristic.*;

public class PseudorandomNumbersTests {
    private static final double ALPHA = 0.95;
    private static final double CUBE_EDGE_SIZE = 1.0;
    private static final int Q = 10;

    private static double seed = 633.6000000000071;

    HistogramDataset dataset = new HistogramDataset();

    public static void seed(double seed1) {
        seed = seed1;
    }

    private static int getMiniCubeNumber(double[] p) {
        int number = 0;

        double m = CUBE_EDGE_SIZE / (double) Q;
        int n = p.length;
        for (int i = 0, d = 1; i < n; i++, d *= Q) {
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

    private static void countMiniCubeDestribution(int[] m, List<double[]> points) {
        int n = points.size();

        for (int i = 0; i < n; i++) {
            int number = getMiniCubeNumber(points.get(i));

            m[number] += 1;
        }
    }

    private static double[] generateValues(int n) {
        PseudorandomNumbers pn = new PseudorandomNumbers(seed);
        double[] values = new double[n];

        for (int i = 0; i < n; i++) {
            values[i] = pn.run();
        }

        return values;
    }

    private static List<double[]> getValueVectors(double[] values, int d) {
        List<double[]> valueVectors = new ArrayList<>();

        double[] v = new double[d];
        int n = values.length;
        for (int i = 0; i < n; i++) {
            int ind = i % d;

            v[ind] = values[i];

            if (ind == d - 1) {
                valueVectors.add(v.clone());
            }
        }

        return valueVectors;
    }

    private static int getFirstDischarge(double num) {
        return String.valueOf(num).charAt(2);
    }

    private static int[] firstDischargeDistribution(double[] values) {
        int m[] = new int[10];

        for (double v : values) {
            m[getFirstDischarge(v)] += 1;
        }

        return m;
    }

    private static int[] getDischarge(double value, int d) {
        String strNum = String.valueOf(value);

        int v[] = new int[8 / d];
        String t = "";
        for (int i = 0; i < 10; i++) {
            t += strNum.charAt(2 + i);

            if (i % d == d - 1) {
                v[i / d] = Integer.valueOf(t);
                t = "";
            }
        }

        return v;
    }

    private static List<int[]> getDischargeVector(double[] values, int d) {
        List<int[]> dischargeVetors = new ArrayList<>();
    
        int n = values.length;
        for (int i = 0; i < n; i++) {
            dischargeVetors.add(getDischarge(values[i], d));
        }

        return dischargeVetors;
    }

    private static int[] gistogramTests(int k) {
        int N = findL(findPeriod1(seed), seed) / k;

        int M = (int) Math.pow(Q, k);
        int[] m = new int[M];

        List<double[]> points = getValueVectors(generateValues(N * k), k);
        countMiniCubeDestribution(m, points);

        double hiSq = countHiSq(m, (double) N / (double) M);

        double hiSqTable = countHiSqTable(ALPHA, M - 2);

        System.out.println(String.format("Хи расчётный (%s) %s Хи табличного (%s)", hiSq, (hiSq > hiSqTable ? ">" : "<"), hiSqTable));

        return m;
    }

    private static void test11() {
        System.out.println("Test 1.1:");

        int period1 = findPeriod1(seed);

        System.out.println("seed = " + seed + ":");
        System.out.println("1: " + period1);
        System.out.println("L: " + findL(period1, seed));
    }

    private static void test12() {
        System.out.println("Test 1.2:");

        gistogramTests(5);
    }

    private static void test21() {
        System.out.println("Test 2.1:");

        int[] m = gistogramTests(1);

        String[] tittle = formTittle(CUBE_EDGE_SIZE, m.length);
    
        buildGistogram(tittle, m, "Одномерный куб");
    }

    private static void test22() {
        System.out.println("Test 2.2:");

        gistogramTests(2);
    }

    private static void test23() {
        System.out.println("Test 2.3:");

        gistogramTests(3);
    }

    private static void test24() {
        System.out.println("Test 2.2:");

        gistogramTests(4);
    }

    private static void test3() {
        System.out.println("Test 3:");

        int q = 10;
        int N = 1000;

        int M = (int) Math.pow(q, 1);
        int[] m = new int[M];

        double prevSeed = seed;
        seed(0.1234567);

        List<double[]> points = getValueVectors(generateValues(N * 1), 1);
        countMiniCubeDestribution(m, points);

        seed(prevSeed);

        double kn = 0;
        double d = N / M;

        for (int mi : m) {
            kn += mi - d;
        }

        kn = ((1 / d) * Math.sqrt(kn / M)) * 100;

        System.out.println("Kн (%): " + kn);
    }

    private static void dischargeTest(int k) {
        int N = 1000000;

        int m[] = new int[10];
        List<int[]> dischargeVectors = getDischargeVector(generateValues(N), k);
    }

    private static void test41() {
        System.out.println("Test 4.1:");

        int N = 1000000;

        double[] values = generateValues(N);

        int m[] = firstDischargeDistribution(values);


    }

    private static void test5() {
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

    private static void printNums() {
        PseudorandomNumbers ps = new PseudorandomNumbers(seed);
        for (int i = 0; i < 600; i++) {
            System.out.println(ps.run());
        }
    }

    /**
     * Find max period L with variable seed, but fixed const of generator.
     */
    public static void findSeedByMaxL() {
        double h = 0.2;

        double goalSeed = 0;
        int maxL = 0;
        double initialSeed = seed;
        for (double s = 0; s < 1000; s += h) {
            seed(s);
            int L = findL(findPeriod1(seed), seed);

            if (L > maxL) {
                maxL = L;
                goalSeed = seed;
            }
        }

        seed(initialSeed);

        System.out.println("Max L is " + maxL + " at seed equals " + goalSeed);
    }

    /**
     * Find max period L with variable const of generator, but fixed seed.
     * Note: Max L is 61710447 at const of generator are equals 67.0, with fixed seed is 633.6000000000071
     */
    public static void findConstByMaxL() {
        double h = 1;

        double goalC = 0;
        int maxL = 0;
        double initialC = PseudorandomNumbers.getC();
        for (double c = 11; c < 100; c += h) {
            PseudorandomNumbers.setC(c);

            System.out.println(c);

            int L = findL(findPeriod1(seed), seed);

            if (L > maxL) {
                maxL = L;
                goalC = c;
            }
        }

        PseudorandomNumbers.setC(initialC);

        System.out.println("Max L is " + maxL + " at const of generator are equals " + goalC + ", with fixed seed is " + seed);
    }

    public static void main(String[] args) {
        PseudorandomNumbers.setC(67.0);

        // test11();

        test21();

        // System.out.println("Period 1: " + findPeriod1ByBruteForce());

        // test3();

        // test23(0);

        // test21(1.0);
    }
}
