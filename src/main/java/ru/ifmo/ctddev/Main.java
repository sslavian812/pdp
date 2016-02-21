package ru.ifmo.ctddev;

import java.util.Arrays;

/**
 * This class is only for sketches and dummy tests.
 * Created by viacheslav on 21.02.2016.
 */
public class Main {
    public static void main(String[] args) {
        int[] a = new int[]{1,2,3};
        int[] b = a.clone();

        b[0]=5;

        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(b));
    }
}
