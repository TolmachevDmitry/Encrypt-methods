package com.tolmic.utils;

public class EulerFunction {

    private static final int CACHE_SIZE = 1000000;
    private static final long[] cache = new long[CACHE_SIZE];
    
    public static long eulerPhi(long n) {
        // Проверяем кэш для небольших чисел
        if (n < CACHE_SIZE && cache[(int)n] != 0) {
            return cache[(int)n];
        }
        
        long result = computeEulerPhi(n);
        
        // Сохраняем в кэш если возможно
        if (n < CACHE_SIZE) {
            cache[(int)n] = result;
        }
        
        return result;
    }
    
    private static long computeEulerPhi(long n) {
        // Реализация основного алгоритма
        long result = n;
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                while (n % i == 0) {
                    n /= i;
                }
                result -= result / i;
            }
        }
        if (n > 1) {
            result -= result / n;
        }
        return result;
    }

}
