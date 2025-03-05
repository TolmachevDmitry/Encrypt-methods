package com.tolmic;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.tolmic.utils.BitsArrayOperationUtils;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class BitsOperationTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testAnd()
    {
        int[] a1 = new int[] {1, 0, 1, 0, 1, 1, 1};
        int[] b1 = new int[] {0, 1, 1, 1, 0, 0, 1};
        int[] c1 = BitsArrayOperationUtils.bitsAnd(a1, b1);

        int[] a2 = new int[] {1, 1, 1, 1, 1, 1, 1};
        int[] b2 = new int[] {1, 1, 1, 1, 1, 1, 1};
        int[] c2 = BitsArrayOperationUtils.bitsAnd(a2, b2);

        int[] a3 = new int[] {0, 0, 0, 0, 1, 0, 0};
        int[] b3 = new int[] {0, 0, 0, 0, 1, 1, 0};
        int[] c3 = BitsArrayOperationUtils.bitsAnd(a3, b3);

        assertArrayEquals(new int[] {0, 0, 1, 0, 0, 0, 1}, c1);
        assertArrayEquals(new int[] {1, 1, 1, 1, 1, 1, 1}, c2);
        assertArrayEquals(new int[] {0, 0, 0, 0, 1, 0, 0}, c3);
    }

    @Test
    public void testOr() {
        int[] a1 = new int[] {1, 0, 1, 0, 1, 1, 1};
        int[] b1 = new int[] {0, 1, 1, 1, 0, 0, 1};
        int[] c1 = BitsArrayOperationUtils.bitsOr(a1, b1);

        int[] a2 = new int[] {0, 1, 0, 1, 1, 0, 1};
        int[] b2 = new int[] {0, 1, 1, 1, 0, 0, 1};
        int[] c2 = BitsArrayOperationUtils.bitsOr(a2, b2);

        int[] a3 = new int[] {0, 0, 0, 0, 1, 0, 0};
        int[] b3 = new int[] {1, 0, 0, 0, 0, 1, 0};
        int[] c3 = BitsArrayOperationUtils.bitsOr(a3, b3);

        assertArrayEquals(new int[] {1, 1, 1, 1, 1, 1, 1}, c1);
        assertArrayEquals(new int[] {0, 1, 1, 1, 1, 0, 1}, c2);
        assertArrayEquals(new int[] {1, 0, 0, 0, 1, 1, 0}, c3);
    }

    @Test
    public void testShiftLeft() {
        int[] x1 = new int[] {1, 0, 1, 0, 1, 1, 1};

        int[] y1 = BitsArrayOperationUtils.shiftLeft(x1, 1);
        int[] y2 = BitsArrayOperationUtils.shiftLeft(x1, 3);
        int[] y3 = BitsArrayOperationUtils.shiftLeft(x1, 5);
        int[] y4 = BitsArrayOperationUtils.shiftLeft(x1, 8);
        int[] y5 = BitsArrayOperationUtils.shiftLeft(x1, 0);

        assertArrayEquals(new int[] {0, 1, 0, 1, 1, 1, 0}, y1);
        assertArrayEquals(new int[] {0, 1, 1, 1, 0, 0, 0}, y2);
        assertArrayEquals(new int[] {1, 1, 0, 0, 0, 0, 0}, y3);
        assertArrayEquals(new int[] {0, 0, 0, 0, 0, 0, 0}, y4);
        assertArrayEquals(new int[] {1, 0, 1, 0, 1, 1, 1}, y5);
    }

    @Test
    public void testShiftRight() {
        int[] x1 = new int[] {1, 0, 1, 0, 1, 1, 1};

        int[] y1 = BitsArrayOperationUtils.shiftRight(x1, 1);
        int[] y2 = BitsArrayOperationUtils.shiftRight(x1, 3);
        int[] y3 = BitsArrayOperationUtils.shiftRight(x1, 5);
        int[] y4 = BitsArrayOperationUtils.shiftRight(x1, 8);
        int[] y5 = BitsArrayOperationUtils.shiftRight(x1, 0);

        assertArrayEquals(new int[] {0, 1, 0, 1, 0, 1, 1}, y1);
        assertArrayEquals(new int[] {0, 0, 0, 1, 0, 1, 0}, y2);
        assertArrayEquals(new int[] {0, 0, 0, 0, 0, 1, 0}, y3);
        assertArrayEquals(new int[] {0, 0, 0, 0, 0, 0, 0}, y4);
        assertArrayEquals(new int[] {1, 0, 1, 0, 1, 1, 1}, y5);
    }

    @Test
    public void testInversion() {
        int[] x1 = new int[] {1, 0, 1, 0, 1, 1, 1};
        int[] x2 = new int[] {0, 0, 1, 1, 1, 0, 0};

        int[] y1 = BitsArrayOperationUtils.bitsInversion(x1);
        int[] y2 = BitsArrayOperationUtils.bitsInversion(x2);

        assertArrayEquals(new int[] {0, 1, 0, 1, 0, 0, 0}, y1);
        assertArrayEquals(new int[] {1, 1, 0, 0, 0, 1, 1}, y2);
    }

    @Test
    public void testXOR() {
        int[] a = new int[] {1, 0, 0, 1, 0, 1, 1, 1};
        int[] b = new int[] {0, 1, 1, 1, 0, 0, 1, 1};

        int[] c = BitsArrayOperationUtils.bitsXOR(a, b);

        assertArrayEquals(c, new int[] {1, 1, 1, 0, 0, 1, 0, 0});
    }

    @Test
    public void testToBitsFromByte() {
        byte num1 = 0;
        byte num2 = 54;
        byte num3 = 123;
        byte num4 = 127;

        int[] bits1 = BitsArrayOperationUtils.toBitsFromByte(num1);
        int[] bits2 = BitsArrayOperationUtils.toBitsFromByte(num2);
        int[] bits3 = BitsArrayOperationUtils.toBitsFromByte(num3);
        int[] bits4 = BitsArrayOperationUtils.toBitsFromByte(num4);

        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, bits1);
        assertArrayEquals(new int[]{0, 1, 1, 0, 1, 1, 0, 0}, bits2);
        assertArrayEquals(new int[]{1, 1, 0, 1, 1, 1, 1, 0}, bits3);
        assertArrayEquals(new int[]{1, 1, 1, 1, 1, 1, 1, 0}, bits4);
    }

    @Test
    public void testAddZeros() {
        int[] bits = new int[] {1, 0, 1, 1, 0};

        bits = BitsArrayOperationUtils.addZeros(bits, 3);

        assertArrayEquals(new int[] {1, 0, 1, 1, 0, 0, 0, 0}, bits);
    }
}
