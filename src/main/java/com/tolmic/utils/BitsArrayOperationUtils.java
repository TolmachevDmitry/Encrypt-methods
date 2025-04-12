package com.tolmic.utils;

public final class BitsArrayOperationUtils {

    public static int[] bitsInversion(int[] x) {
        int n = x.length;

        int[] bits = new int[n];
        for (int i = 0; i < n; i++) {
            bits[i] = x[i] == 0 ? 1 : 0;
        }

        return bits;
    }

    public static int[] shiftRight(int[] x, int d) {
        int n = x.length;

        int[] bits = new int[n];
        for (int i = d; i < n; i++) {
            bits[i] = x[i - d];
        }

        return bits;
    }

    public static int[] shiftLeft(int[] x, int d) {
        int n = x.length;

        int[] bits = new int[n];
        for (int i = d; i < n; i++) {
            bits[i - d] = x[i];
        }

        return bits;
    }

    public static int[] bitsAnd(int[] a, int[] b) {
        int n = a.length;

        int[] and = new int[n];
        for (int i = 0; i < n; i++) {
            and[i] = a[i] & b[i];
        }

        return and;
    }

    public static int[] bitsOr(int[] a, int[] b) {
        int n = a.length;

        int[] and = new int[n];
        for (int i = 0; i < n; i++) {
            and[i] = a[i] | b[i];
        }

        return and;
    }

    public static int[] bitsXOR(int[] a, int[] b) {
        int n = a.length;

        int[] bits = new int[n];
        for (int i = 0; i < n; i++) {
            bits[i] = a[i] != b[i] ? 1 : 0;
        }

        return bits;
    }

    public static int[] addZeros(int[] x, int zeroNum) {
        int n = x.length;

        int[] t = new int[n + zeroNum];

        for (int i = 0; i < n; i++) {
            t[i] = x[i];
        }

        return t;
    }

    private static int[] toBitsFromByteMain(byte b) {
        int[] bits = new int[8];

        int num = b;
        int i = 0;
        while (num > 0) {
            bits[i] = num % 2;

            num /= 2;
            i += 1;
        }

        return bits;
    }

    public static int[] toBitsFromByte(byte b) {
        return toBitsFromByteMain(b);
    }

    public static int[] getBitSequence(byte[] bytes) {
        int n = bytes.length;

        int[] bits = new int[n * 8];
        for (int i = 0; i < n; i++) {
            int[] t = toBitsFromByteMain(bytes[i]);

            for (int j = 0; j < 8; j++) {
                bits[i * 8 + j] = t[j];
            }
        }

        return bits;
    }

    public static int[] checkSizeFormat(int[] data) {
        int n = data.length;
        int twoPower = 1;

        if (n < 64) {
            return addZeros(data, 64 - n);
        }
        
        while (twoPower < n) {
            twoPower *= 2;
        }

        if (twoPower > n) {
            data = addZeros(data, twoPower - n);
        }

        return data;
    }

    public static byte[] getByteFromBits(int[] bits) {
        int n = bits.length / 8;

        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++) {
            
        }

        return bytes;
    }

    public static String convertToString(int[] bits) {
        String str = "";

        int n = bits.length;
        for (int i = 0; i < n; i++) {
            str += bits[i];
        }

        return str;
    }
}
