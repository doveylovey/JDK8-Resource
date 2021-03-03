package com.study.test.gc;

public class ClassTest {
    public static void main(String[] args) {
        String stringA = stringA("参数1", 2);
        System.out.println(stringA);

        ClassTest classTest = new ClassTest();
        Integer integerA = classTest.integerA();
        System.out.println(integerA);
    }

    public static String stringA(String param1, Integer param2) {
        String prefix = "前缀";
        Integer suffix = 1000;
        return prefix + param1 + param2 + suffix;
    }

    public Integer integerA() {
        return Math.round(10.50F);
    }
}
