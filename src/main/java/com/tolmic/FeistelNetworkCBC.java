package com.tolmic;

import static com.tolmic.utils.BitsArrayOperationUtils.checkSizeFormat;
import static com.tolmic.utils.ArrayUtils.valuesOfInteger;
import static com.tolmic.utils.BitsArrayOperationUtils.getBitSequence;

import static com.tolmic.utils.BitsStringOperationUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeistelNetworkCBC {

    private static class Code {
        String data;
        List<String> keySequence;

        public Code(String data, List<String> keySequence) {
            this.data = data;
            this.keySequence = keySequence;
        }
    }

    private static final int KEY_SIZE = 64;
    private static final int BLOCK_SIZE = 32;
    private static final int RAUND_NUMBER = 12;
    private static final int BLOCK_NUMBER = 2;

    private static String F(String L, String K) {
        String lAndK = and(L, K);
        String lShiftedLeft = shiftLeft(L, 4);

        String or = or(lAndK, lShiftedLeft);
        String kLeftShifted = shiftLeft(K, 10);

        return or(or, kLeftShifted);
    }

    private static String createRandomKey() {
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < KEY_SIZE; i++) {
            sb.append(Math.random() >= 0.5 ? "1" : "0");
        }

        return sb.toString();
    }

    private static HashMap<Integer, String> getBlocks(String data, int num) {
        HashMap<Integer, String> blocks = new HashMap<>();

        for (int i = 1; i <= BLOCK_NUMBER; i++) {
            blocks.put(i, data.substring(num + (i - 1) * BLOCK_SIZE, num + i * BLOCK_SIZE));
        }

        return blocks;
    }

    private static void mixBlocks(HashMap<Integer, String> blocks) {
        for (int i = 1; i < BLOCK_NUMBER; i++) {
            String t = blocks.get(i);
            
            blocks.put(i, blocks.get(i + 1));
            blocks.put(i + 1, t);
        }
    }

    private static void useVectoreFour(HashMap<Integer, String> blocks, String vector) {
        for (int i = 1; i <= BLOCK_NUMBER; i++) {
            String b = blocks.get(i);
            String v = vector.substring((i - 1) * BLOCK_SIZE, i * BLOCK_SIZE);
            String XOR = xor(b, v);

            blocks.put(i, XOR);
        }
    }

    private static void solveSchemeBody(HashMap<Integer, String> blocks, String key) {
        String F = F(blocks.get(1), key);
        String XOR = xor(blocks.get(2), F);
        blocks.put(2, XOR);
    }

    private static String concatenateBlocks(HashMap<Integer, String> blocks) {
        StringBuilder sb = new StringBuilder("");

        for (int i = 1; i <= BLOCK_NUMBER; i++) {
            sb.append(blocks.get(i));
        }

        return sb.toString();
    }

    private static String solveScheme(String data, String key, String vectorFour, boolean isLastRaund) {
        int n = data.length();
        int h = BLOCK_NUMBER * BLOCK_SIZE;
        String v = vectorFour;

        StringBuilder bitChain = new StringBuilder("");

        for (int i = 0; i < n; i += h) {
            HashMap<Integer, String> blocks = getBlocks(data, i);
            useVectoreFour(blocks, v);

            solveSchemeBody(blocks, key);

            if (!isLastRaund) {
                mixBlocks(blocks);
            }

            v = concatenateBlocks(blocks);
            bitChain.append(v);
        }

        return bitChain.toString();
    }

    private static String solveSchemeForDecrypt(String data, String key, String vectorFour, boolean isLastRaund) {
        int n = data.length();
        int h = BLOCK_NUMBER * BLOCK_SIZE;
        String v = vectorFour;

        StringBuilder bitsChain = new StringBuilder("");

        for (int i = 0; i < n; i += h) {
            HashMap<Integer, String> blocks = getBlocks(data, i);

            String encryptedBlock = concatenateBlocks(blocks);

            if (!isLastRaund) {
                mixBlocks(blocks);
            }

            solveSchemeBody(blocks, key);
            useVectoreFour(blocks, v);
            bitsChain.append(concatenateBlocks(blocks));

            v = encryptedBlock;
        }

        return bitsChain.toString();
    }

    public static Code encrypt(String data, String vectorFour) {
        String key = createRandomKey();

        List<String> keysSequence = new ArrayList<>();

        for (int i = 0; i < RAUND_NUMBER; i++) {
            String currKey = shiftRight(key, i * 3);
            keysSequence.add(currKey);

            data = solveScheme(data, currKey, vectorFour, i == RAUND_NUMBER - 1);
        }

        return new Code(data, keysSequence);
    }

    public static String decrypt(String data, List<String> keys, String vectorFour) {
        for (int i = 0; i < RAUND_NUMBER; i++) {
            data = solveSchemeForDecrypt(data, keys.get(RAUND_NUMBER - i - 1), vectorFour, i == 0);
        }

        return data;
    }

    private static void printResults(String data, String messege) {
        System.out.println(messege + ": " + data);
    }

    public static void main(String[] args) {
        String messege = "Hello, Vanya !";

        byte[] bytes = messege.getBytes();

        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }
        System.out.println();

        String data = valuesOfInteger(checkSizeFormat(getBitSequence(bytes)));

        String vectorFour = valuesOfInteger(getBitSequence("Vector-four".getBytes()));

        printResults(vectorFour, "Vector IV");

        printResults(data, "Input bits sequence");

        Code code = encrypt(data, vectorFour);

        printResults(code.data, "Encrypted bits sequence");

        String decrypted = decrypt(code.data, code.keySequence, vectorFour);

        printResults(decrypted, "Decrypted bits sequence");

        if (decrypted.equals(data)) {
            System.out.println("Descrypting is true");
        } else {
            System.out.println("Descrypting is false");
        }
    }
}
