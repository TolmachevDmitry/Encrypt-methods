package com.tolmic;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class Draft {

    public static void f(String[] args) throws IOException {
        // 986575
        // 0.3541388581367926
        // PseudorandomNumbers pn = new PseudorandomNumbers(23);
        // int j = 0;
        // int a = 0;
        // for (int i = 1; true; i++) {
        //     double num = pn.run();

        //     if (num == 0.3541388581367926) {
        //         j += 1;

        //         if (j == 2) {
        //             System.out.println(i - a);
        //             break;
        //         }

        //         a = i;
        //     }
        // }

        // Set<String> set = new TreeSet<>();
        // PseudorandomNumbers pn = new PseudorandomNumbers(23);
        // for (int i = 1; i < 900000000; i++) {
        //     String num = String.valueOf(pn.run());
            
        //     if (set.contains(num)) {
        //         System.out.println(i);
        //         System.out.println(num);
        //         break;
        //     }

        //     set.add(num);
        // }
    }

    public static void main(String[] args) {
        // Set<Integer> set = new TreeSet<>((a, b) -> {
        //     if (a < b && b - a > 6) {
        //         return -1;
        //     } else if (a > b && a - b > 6) {
        //         return 1;
        //     }
        //     return 0;
        // });

        // set.add(2);
        // System.out.println(set.contains(4));
        // System.out.println(set.contains(12));
        // System.out.println(set.contains(6));
        // System.out.println(set.contains(20));

        PseudorandomNumbers ps = new PseudorandomNumbers(12);
        for (int i = 0; i < 20; i++) {
            System.out.println(i % 5);
        }
    }

}
