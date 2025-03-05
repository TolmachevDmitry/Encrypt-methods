package com.tolmic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tolmic.utils.BitsArrayOperationUtils.*;
import static com.tolmic.utils.ArrayUtils.*;

/**
 * @author Dmitry Tolmachev
 */
public class FeistelNetwork {

    protected static class Code {
        int[] data;
        List<int[]> keySequence;

        public Code(int[] data, List<int[]> keySequence) {
            this.data = data;
            this.keySequence = keySequence;
        }
    }

    private static final int KEY_SIZE = 64;
    private static final int BLOCK_SIZE = 32;
    private static final int RAUND_NUMBER = 12;
    private static final int BLOCK_NUMBER = 2;

    /**
     * F_26(L, K) = ((L and K) or (L << 4)) or (K << 10)
     * 
     * @param L
     * @param K
     * @return
     */
    private static int[] F(int[] L, int[] K) {
        int[] lAndK = bitsAnd(L, K);
        int[] lShiftedLeft = shiftLeft(L, 4);

        int[] or = bitsOr(lAndK, lShiftedLeft);
        int[] kLeftShifted = shiftLeft(K, 10);

        return bitsOr(or, kLeftShifted);
    }

    private static int[] createRandomKey() {
        int[] key = new int[KEY_SIZE];

        for (int i = 0; i < KEY_SIZE; i++) {
            if (Math.random() >= 0.5) {
                key[i] = 1;
            }
        }

        return key;
    }

    private static HashMap<Integer, int[]> getBlocks(int[] data, int num) {
        HashMap<Integer, int[]> blocks = new HashMap<>(); 
        
        for (int i = 1; i <= BLOCK_NUMBER; i++) {
            blocks.put(i, splitArray(data, num + (i - 1) * BLOCK_SIZE, num + i * BLOCK_SIZE - 1));
        }

        return blocks;
    }

    private static void putBlocksToChain(List<int[]> allBlocks, HashMap<Integer, int[]> blocks) {
        for (int i = 1; i <= BLOCK_NUMBER; i++) {
            allBlocks.add(blocks.get(i));
        }
    }

    private static void solveSchemeBody(HashMap<Integer, int[]> blocks, int[] key) {
        int[] F     = F(blocks.get(1), key);
        int[] XOR   = bitsXOR(F, blocks.get(2));

        blocks.put(2, XOR);
    }

    private static void mixBlocks(HashMap<Integer, int[]> blocks) {
        for (int i = 1; i < BLOCK_NUMBER; i++) {
            changeArrays(blocks.get(i), blocks.get(i + 1));
        }
    }

    private static int[] solveScheme(int[] data, int[] key, boolean isLastRaund) {
        int n = data.length;

        int h = BLOCK_NUMBER * BLOCK_SIZE;

        List<int[]> allBlocks = new ArrayList<>();
        for (int i = 0; i < n; i += h) {
            HashMap<Integer, int[]> blocks = getBlocks(data, i);

            solveSchemeBody(blocks, key);

            if (!isLastRaund) {
                mixBlocks(blocks);
            }

            putBlocksToChain(allBlocks, blocks);
        }

        return fromListArray(allBlocks);
    }

    /**
     * 
     * @param data - sequence of bits
     * @return secret key
     */
    public static Code encrypt(int[] data) {
        int[] key = createRandomKey();
        int[] currKey = key.clone();

        List<int[]> keysSequence = new ArrayList<>();

        int[] encrypted = data.clone();

        for (int i = 0; i < RAUND_NUMBER; i++) {
            currKey = shiftRight(key, i * 3);
            keysSequence.add(currKey);

            encrypted = solveScheme(encrypted, key, i == RAUND_NUMBER - 1);
        }

        return new Code(encrypted, keysSequence);
    }

    /**
     * 
     * @param data
     * @param keys
     */
    public static int[] decrypt(int[] data, List<int[]> keys) {
        int[] decrypted = data.clone(); 

        for (int i = 0; i < RAUND_NUMBER; i++) {
            decrypted = solveScheme(decrypted, keys.get(RAUND_NUMBER - i - 1), i == RAUND_NUMBER - 1);
        }

        return decrypted;
    }
}
