package com.tolmic;

import static com.tolmic.utils.MathUtils.eulerFunc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;;

public final class RSADescrypt {

    public static String encrypt(List<BigInteger> M, RSAKey K) {
        BigInteger n = new BigInteger(String.valueOf(K.getN()));
        BigInteger e = new BigInteger(String.valueOf(K.getE()));

        return M.stream().map(m -> m.modPow(e, n).toString()).collect(Collectors.joining());
    }

    private static char extractLastSymb(List<BigInteger> mParts) {
        int lastPartNum = mParts.size() - 1;

        String str = mParts.get(lastPartNum).toString();
        int lastCharNum = str.length() - 1;

        mParts.set(lastPartNum, new BigInteger(str.substring(0, lastCharNum)));

        return str.charAt(lastCharNum);
    }

    private static String joinMessegeParts(List<BigInteger> mParts) {
        return mParts.stream().map(m -> m.toString()).collect(Collectors.joining());
    }

    private static void printParts(List<BigInteger> parts) {
        System.out.println("Parts of descrypted messege:");
        for (BigInteger p : parts) {
            System.out.println(p);
        }
        System.out.println();
    }

    private static List<BigInteger> splitMessege(String C, long n) {
        List<BigInteger> mParts = new ArrayList<>();

        int size = C.length();

        String t = "";
        for (int i = 0; i < size; i++) {
            String currSymb = String.valueOf(C.charAt(i));

            if (t.isEmpty() && i > 0 && currSymb.equals("0")) {
                t += extractLastSymb(mParts);
            }

            t += currSymb;

            if (i == size - 1 || i < size - 1 && Long.valueOf(t + C.charAt(i + 1)) > n) {
                mParts.add(new BigInteger(t));
                t = "";
            }
        }

        return mParts;
    }

    public static List<BigInteger> decrypt(String C, RSAKey K) {
        BigInteger minusOne = new BigInteger("-1");

        List<BigInteger> cParts = splitMessege(C, K.getN());
        printParts(cParts);

        BigInteger n = new BigInteger(String.valueOf(K.getN()));
        BigInteger e = new BigInteger(String.valueOf(K.getE()));

        BigInteger eulerN = fromLongToBigInteger(eulerFunc(K.getN()));

        BigInteger d = e.modPow(minusOne, eulerN);

        return cParts.stream().map(m -> m.modPow(d, n)).collect(Collectors.toList());
    }

    public static BigInteger fromLongToBigInteger(double num) {
        String strNum = String.valueOf(num);

        int eIndex = strNum.indexOf("E");
        eIndex = eIndex < 0 ? strNum.length() : eIndex;

        String normNum = strNum.substring(0, eIndex).replace(".", "");

        normNum += "0000000";

        return new BigInteger(normNum);
    }

    public static void main(String[] args) {
        RSAKey rsaKey = new RSAKey(471110543304749L, 12397);
        String C = "229400309539826616904080414433914229452297284548923537277210088832206760805";

        System.out.println("Initial C: " + C);

        List<BigInteger> decrypted = decrypt(C, rsaKey);
        System.out.println("Decripted all messege: " + joinMessegeParts(decrypted));

        String encrypted = encrypt(decrypted, rsaKey);

        System.out.println("Ecnrypted: " + encrypted);
        System.out.println();

        if (C.equals(encrypted)) {
            System.out.println("Initial C and encrypted is equals !");
        } else {
            System.out.println("Initial C and ecnrypted IS NOT equals !");
        }
    }
}
