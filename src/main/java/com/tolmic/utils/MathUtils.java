package com.tolmic.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

public class MathUtils {

    public static double eulerFunc(long num) {
        return EulerFunction.eulerPhi(num);
    }

    private static List<String> multiplyPart(char[] a, char[] b) {
        List<String> multiplyResults = new ArrayList<>(100);

        int n = a.length;
        int m = b.length;

        for (int i = m - 1; i >= 0; i--) {
            int v2 = toIntFromChar(b[i]);
            int sign = 0;

            String multiplyResult = "";

            for (int j = n - 1; j >= 0; j--) {
                int v1 = toIntFromChar(a[j]);

                int r = v1 * v2 + sign;
                sign = r / 10;
                multiplyResult = (j == 0 ? r : r % 10) + multiplyResult;
            }

            multiplyResults.add(multiplyResult);
        }

        return multiplyResults;
    }

    private static String plusPart(List<String> multiplyResults) {
        int m = multiplyResults.size();

        String result = "";
        int sign = 0;
        int prevLength = 1;
        for (int i = 0; i < m; i++) {
            int currLength = multiplyResults.get(i).length();
            int startIndex = currLength - prevLength;
            int shift = currLength - startIndex - 1;

            if (startIndex < 0 && sign != 0 && i == m - 1) {
                result = sign + result;
                return result;
            }

            for (int j = startIndex; j >= 0; j--) {
                int sum = 0;

                for (int k = i; k < m; k++) {
                    String r = multiplyResults.get(k);
                    int rSize = r.length();

                    int ind = rSize - 1 + (k - i) - shift;
                    if (ind < rSize) {
                        sum += toIntFromChar(r.charAt(ind));
                    }
                }

                int fullSum = sum + sign;
                result = (j == 0 && i == m - 1 ? fullSum : fullSum % 10) + result;
                sign = fullSum / 10;

                shift += 1;
            }

            prevLength = currLength;
        }

        return result;
    }

    private static int toIntFromChar(char v) {
        return Integer.valueOf(String.valueOf(v));
    }

    public static String multiply(String x1, String x2) {
        char[] a = new String(x1.length() > x2.length() ? x1 : x2).toCharArray();
        char[] b = new String(x1.length() > x2.length() ? x2 : x1).toCharArray();

        List<String> multiplyResults = multiplyPart(a, b);
        String commonResult = plusPart(multiplyResults);

        return commonResult;
    }

    public static String pow(String a, int p) {
        String result = "1";

        for (int i = 1; i <= p; i++) {
            result = multiply(result, a);
            System.out.println(i);
        }

        return result;
    }

    public static double countHiSqTable(double alpha, double d) {
        ChiSquaredDistribution distribution = new ChiSquaredDistribution(d);
        return distribution.inverseCumulativeProbability(1 - alpha);
    }

    public static double mean(double[] data) {
        return Arrays.stream(data).map(e -> e / data.length).sum();
    }

    public static double sum(double[] data) {
        return Arrays.stream(data).sum();
    }

    public static double error(double a, double b) {
        return Math.abs(a - b);
    }

}
