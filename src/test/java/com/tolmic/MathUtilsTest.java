package com.tolmic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tolmic.utils.MathUtils;

public class MathUtilsTest {

    @Test
    public void multiplyTest() {
        for (int i = 1; i < 1000; i++) {
            for (int j = 1; j < 1000; j++) {
                int expected = i * j;
                int actual = Integer.valueOf(MathUtils.multiply(i + "", j + ""));
                
                assertEquals(actual, expected);
            }
        }
    }

}


