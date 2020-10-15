package com.study.test.base;

public class TestVariableModifier {
    public static void main(String[] args) {
        System.out.println(TestSub.str);
    }
}

class TestParent {
    //public static String str = "1";// 输出 21
    //public static final String str = "1";// 输出 1

    static {
        System.out.println("2");
    }
}

class TestSub extends TestParent {
    //public static String str = "1";// 输出 231
    public static final String str = "1";// 输出 1
    static {
        System.out.println("3");
    }
}