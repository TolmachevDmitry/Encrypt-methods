package com.tolmic;

import static com.tolmic.utils.MathUtils.eulerFunc;

import java.math.BigInteger;;

public class RSADescrypt {

    public static BigInteger ecrypt(RSAKey K, BigInteger M) {
        BigInteger n = new BigInteger(String.valueOf(K.getN()));
        BigInteger e = new BigInteger(String.valueOf(K.getE()));

        return M.modPow(e, n);
    }

    public static BigInteger decrypt(RSAKey K, BigInteger C) {
        BigInteger minusOne = new BigInteger("-1");

        BigInteger n = new BigInteger(String.valueOf(K.getN()));
        BigInteger e = new BigInteger(String.valueOf(K.getE()));

        BigInteger eulerN = fromLongToBigInteger(eulerFunc(K.getN()));

        BigInteger d = e.modPow(minusOne, eulerN);

        return C.modPow(d, n);
    }

    public static BigInteger fromLongToBigInteger(double num) {
        String strNum = String.valueOf(num);

        int eIndex = strNum.indexOf("E");
        eIndex = eIndex < 0 ? strNum.length() : eIndex;

        String normNum = "";
        for (int i = 0; i < eIndex; i++) {
            String symb = String.valueOf(strNum.charAt(i));

            if (!symb.equals(".")) {
                normNum += symb;
            }
        }

        int m = 15 - normNum.length();
        for (int i = 0; i < m; i++) {
            normNum += "0";
        }

        return new BigInteger(normNum);
    }

    public static void main(String[] args) {
        RSAKey rsaKey = new RSAKey(471090785117207L, 12377);
        BigInteger C = new BigInteger("314999112281065205361706341517321987491098667");
        System.out.println("Given C: " + C);

        BigInteger M = decrypt(rsaKey, C);
        System.out.println("M: " + M);

        System.out.println(M.pow(12377));

        // BigInteger C1 = ecrypt(rsaKey, M);
        // System.out.println("C: " + C1);

        // if (C.equals(C1)) {
        //     System.out.println("Encrypted M is equals given C");
        // } else {
        //     System.out.println("Wrong !");
        // }
    }
}
