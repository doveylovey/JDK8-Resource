package com.study.test.jvm;

public class JvmTests {
    private static int count = 0;

    public static void main(String[] args) {
        recursion(0, false, 0);
        //recursion();
    }

    public static void recursion() {
        System.out.println("count=" + count);
        count++;
        recursion();
    }

    public static void recursion(Integer integerA, boolean booleanB, int c) {
        long long1 = 12;
        short short1 = 1;
        byte byte1 = 1;
        String string1 = "1";
        System.out.println("count=" + count);
        count++;
        recursion(1, true, 3);
    }
}
