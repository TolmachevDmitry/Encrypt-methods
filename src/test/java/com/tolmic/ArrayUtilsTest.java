package com.tolmic;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.tolmic.utils.ArrayUtils;

public class ArrayUtilsTest {

    @Test
    public void splitTest() {
        int[] arr = new int[] {1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1};

        int[] arr1 = ArrayUtils.splitArray(arr, 0, 2);
        int[] arr2 = ArrayUtils.splitArray(arr, 4, 8);

        assertArrayEquals(new int[] {1, 0, 0},      arr1);
        assertArrayEquals(new int[] {1, 0, 0, 0, 1},   arr2);
    }

    @Test
    public void putToArrayTest() {
        int[] arr = new int[] {0, 0, 0, 0, 0, 0, 0};

        int[] x1 = new int[] {1, 0, 1};
        int[] x2 = new int[] {0, 1, 1};

        ArrayUtils.putToArray(arr, x1, 0, 2);
        ArrayUtils.putToArray(arr, x2, 4, 6);

        assertArrayEquals(new int[] {1, 0, 1, 0, 0, 1, 1}, arr);
    }

    @Test
    public void fromListArrayTest() {
        List<int[]> arrays1 = Arrays.asList(new int[] {1, 1, 1}, new int[] {2, 2, 2}, new int[] {3, 3, 3});
        int[] arr1 = ArrayUtils.fromListArray(arrays1);

        List<int[]> arrays2 = Arrays.asList(new int[] {1}, new int[] {2, 3}, new int[] {4, 5, 6});
        int[] arr2 = ArrayUtils.fromListArray(arrays2);


        assertArrayEquals(new int[] {1, 1, 1, 2, 2, 2, 3, 3, 3}, arr1);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6}, arr2);
    }

}
