package com.tolmic;

import static com.tolmic.utils.BitsArrayOperationUtils.checkSizeFormat;
import static com.tolmic.utils.ArrayUtils.valuesOfInteger;
import static com.tolmic.utils.BitsArrayOperationUtils.getBitSequence;

import static com.tolmic.utils.BitsStringOperationUtils.*;

import java.util.HashMap;

public class FeistelNetworkCFB {

    private static class Code {
        String penultimateText;
        String lastText;

        public Code(String penultimateText, String lastText) {
            this.penultimateText = penultimateText;
            this.lastText = lastText;
        }
    }

    private static final int KEY_SIZE = 64;
    private static final int BLOCK_SIZE = 32;
    private static final int RAUND_NUMBER = 1;
    private static final int BLOCK_NUMBER = 2;

    private static String F(String L, String K) {
        String lAndK = and(L, K);
        String lShiftedLeft = shiftLeft(L, 4);

        String or = or(lAndK, lShiftedLeft);
        String kLeftShifted = shiftLeft(K, 10);

        return or(or, kLeftShifted);
    }

    private static HashMap<Integer, String> getBlocks(String data) {
        HashMap<Integer, String> blocks = new HashMap<>();

        for (int i = 1; i <= BLOCK_NUMBER; i++) {
            blocks.put(i, data.substring((i - 1) * BLOCK_SIZE, i * BLOCK_SIZE));
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

    private static void solveSchemeBody(HashMap<Integer, String> blocks, String key) {
        String F = F(blocks.get(1), key);
        String XOR = xor(blocks.get(2), F);
        blocks.put(2, XOR);
    }

    private static void useOpenText(HashMap<Integer, String> vectorsBlocks, String data, int num) {
        for (int i = 1; i <= BLOCK_NUMBER; i++) {
            String b = vectorsBlocks.get(i);
            String v = data.substring((i - 1) * BLOCK_SIZE, i * BLOCK_SIZE);

            String XOR = xor(b, v);

            vectorsBlocks.put(i, XOR);
        }
    }

    private static String concatenateBlocks(HashMap<Integer, String> blocks) {
        StringBuilder sb = new StringBuilder("");

        for (int i = 1; i <= BLOCK_NUMBER; i++) {
            sb.append(blocks.get(i));
        }

        return sb.toString();
    }

    private static Code solveScheme(String data, String key, String initVector, boolean isLastRaund) {
        int n = data.length();
        int h = BLOCK_NUMBER * BLOCK_SIZE;
        String v = initVector;

        StringBuilder ecryptedChain = new StringBuilder("");
        StringBuilder xorChain = new StringBuilder("");

        for (int i = 0; i < n; i += h) {
            HashMap<Integer, String> blocks = getBlocks(v);

            solveSchemeBody(blocks, key);

            if (!isLastRaund) {
                mixBlocks(blocks);
            }

            ecryptedChain.append(concatenateBlocks(blocks));

            useOpenText(blocks, data, i);
            v = concatenateBlocks(blocks);

            xorChain.append(v);
        }

        return new Code(ecryptedChain.toString(), xorChain.toString());
    }

    public static Code encrypt(String data, String vectorForEncrypt) {
        // String key = createRandomBitsChain(KEY_SIZE);
        String key = "11111111";

        Code code = new Code(vectorForEncrypt, vectorForEncrypt);

        for (int i = 0; i < RAUND_NUMBER; i++) {
            String currKey = shiftRight(key, i * 3);

            code = solveScheme(data, currKey, code.lastText, i == RAUND_NUMBER - 1);
        }

        return code;
    }

    public static String decrypt(Code code) {
        return xor(code.lastText, code.penultimateText);
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

        // String data = valuesOfInteger(checkSizeFormat(getBitSequence(bytes)));
        String data = "01101110";

        // String initVector = createRandomBitsChain(data.length());

        String initVector = "11111111";

        printResults(initVector, "Initialization vector");

        printResults(data, "Input bits sequence");

        Code code = encrypt(data, initVector);

        printResults(code.lastText, "Encrypted bits sequence");

        String decrypted = decrypt(code);

        printResults(decrypted, "Decrypted bits sequence");

        if (decrypted.equals(data)) {
            System.out.println("Descrypting is true");
        } else {
            System.out.println("Descrypting is false");
        }
    }
}