package com.tolmic.utils;

import java.util.List;

public final class ArrayUtils {

    public static int[] splitArray(int[] arr, int fromIndex, int toIndex) {
        int[] cut = new int[toIndex - fromIndex + 1];

        for (int i = fromIndex; i <= toIndex; i++) {
            cut[i - fromIndex] = arr[i]; 
        }
        
        return cut;
    }

    public static void putToArray(int[] goalArray, int[] x, int fromIndex, int toIndex) {
        for (int i = fromIndex; i <= toIndex; i++) {
            goalArray[i] = x[i - fromIndex];
        }
    }
    
    public static void changeArrays(int[] a, int[] b) {
        int n = a.length;

        for (int i = 0; i < n; i++) {
            int t = a[i];

            a[i] = b[i];
            b[i] = t;
        }
    }

    public static int[] fromListArray(List<int[]> arrays) {
        int n = 0;
        int size = arrays.size();

        for (int i = 0; i < size; i++) {
            n += arrays.get(i).length;
        }

        int[] arr = new int[n];
        int m = 0;
        for (int i = 0; i < size; i++) {
            int[] t = arrays.get(i);

            for (int j = 0; j < t.length; j++) {
                arr[m + j] = t[j];
            }

            m += t.length;
        }

        return arr;
    }

    public static String valuesOfInteger(int[] arr) {
        StringBuilder sb = new StringBuilder("");

        for (int b : arr) {
            sb.append(b);
        }

        return sb.toString();
    }
}
