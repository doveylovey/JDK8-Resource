package com.resource.test;

public class TempTests {
    public static boolean method() {
        Boolean a = false;
        try {
            for (int i = 0; ; i++) {
                if (i == Integer.MAX_VALUE) {
                    return true;
                }
            }
        } finally {
            a = null;
        }
    }

    public static void main(String[] args) {
        System.out.println(TempTests.method());
    }
}
