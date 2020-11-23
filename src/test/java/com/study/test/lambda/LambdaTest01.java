package com.study.test.lambda;

import org.junit.Test;

/**
 * 支持 lambda 表达式的接口只允许定义一个抽象方法：即带有 @FunctionalInterface 注解的接口只允许定义一个抽象方法
 */
public class LambdaTest01 {
    @Test
    public void test01() {
        // lambda 表达式实现接口 A：没有指定具体类型
        A a1 = (param1, param2) -> {
            return param1 + param2;
        };
        System.out.println(a1.a(10, 2));

        // lambda 表达式实现接口 A：接收指定类型的参数
        A a2 = (int param1, int param2) -> {
            return param1 - param2;
        };
        System.out.println(a2.a(10, 2));

        A a3 = (int param1, int param2) -> param1 * param2;
        System.out.println(a3.a(10, 2));

        A a4 = (param1, param2) -> param1 / param2;
        System.out.println(a4.a(10, 2));

        new B() {
            @Override
            public void b(String param) {
                System.out.println(param);
            }
        }.b("hello");

        B b1 = (String str) -> {
            System.out.println(str);
        };
        b1.b("hello b1");

        B b2 = (param) -> System.out.println(param);
        b2.b("hello b2");

        // 匿名内部类(无参)
        String c = new C() {
            @Override
            public String c() {
                return "hello c";
            }
        }.c();
        System.out.println(c);

        // lambda 表达式
        C c1 = () -> "hello c1";
        System.out.println(c1.c());

        // 匿名内部类(有参)
        String d = new D() {
            @Override
            public String d(String param) {
                return "hello " + param;
            }
        }.d("d");
        System.out.println(d);

        // lambda 表达式
        D d1 = (param) -> "hello " + param;
        System.out.println(d1.d("d1"));
    }
}


interface A {
    int a(int param1, int param2);
}

interface B {
    void b(String param);
}

interface C {
    String c();
}

interface D {
    String d(String param);
}