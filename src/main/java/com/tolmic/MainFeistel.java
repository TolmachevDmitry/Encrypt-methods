package com.tolmic;

import com.tolmic.FeistelNetwork.Code;

import static com.tolmic.utils.BitsArrayOperationUtils.getBitSequence;
import static com.tolmic.utils.BitsArrayOperationUtils.checkSizeFormat;

public class MainFeistel {

    private static void printResults(int[] data, String messege) {
        int n = data.length;

        System.out.println(messege + ":");
        for (int i = 0; i < n; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String messege = "Hello, world !";

        byte[] bytes = messege.getBytes();

        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }
        System.out.println();

        int[] data = getBitSequence(bytes);

        data = checkSizeFormat(data);

        printResults(data, "Input bits sequence");

        Code code = FeistelNetwork.encrypt(data);

        printResults(code.data, "Encrypted bits sequence");

        int[] decrypted = FeistelNetwork.decrypt(code.data, code.keySequence);

        printResults(decrypted, "Decrypted bits sequence");

        for (int i = 0; i < decrypted.length; i++) {
            if (decrypted[i] != data[i]) {
                System.out.println(String.format("In %s expected [%s], but taken [%s]", i, data[i], decrypted[i]));
                break;
            }
        }
    }
}
