package com.tolmic.utils;

public final class BitsStringOperationUtils {

    public static String and(String a, String b) {
        int n = a.length();

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < n; i++) {
            sb.append(String.valueOf(a.charAt(i)).equals(String.valueOf((b.charAt(i)))) ? "1" : "0");
        }

        return sb.toString();
    }

    public static String or(String a, String b) {
        int n = a.length();

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < n; i++) {
            if (!String.valueOf(a.charAt(i)).equals("0") || !String.valueOf(b.charAt(i)).equals("0")) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }

        return sb.toString();
    }

    public static String shiftLeft(String x, int h) {
        int n = x.length();

        char[] c = new char[n];
        for (int i = 0; i < n; i++) {
            c[i] = '0';
        }

        for (int i = h; i < n; i++) {
            c[i - h] = x.charAt(i);
        }

        return String.valueOf(c);
    }

    public static String shiftRight(String x, int h) {
        int n = x.length();

        char[] c = new char[n];
        for (int i = 0; i < n; i++) {
            c[i] = '0';
        }

        for (int i = h; i < n; i++) {
            c[i] = x.charAt(i - h);
        }

        return String.valueOf(c);
    }

    public static String inversion(String x) {
        int n = x.length();

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < n; i++) {
            if (String.valueOf(x.charAt(i)).equals("0")) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }

        return sb.toString();
    }

    public static String xor(String a, String b) {
        int n = a.length();

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < n; i++) {
            if (String.valueOf(a.charAt(i)).equals(String.valueOf(b.charAt(i)))) {
                sb.append("0");
            } else {
                sb.append("1");
            }
        }

        return sb.toString();
    }

    public static String createRandomBitsChain(int chainSize) {
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < chainSize; i++) {
            sb.append(Math.random() >= 0.5 ? "1" : "0");
        }

        return sb.toString();
    }
}
