package com.study.test.temp;

import org.junit.Test;

public class IntegerTests {
    @Test
    public void test01() {
        int intA = 100;
        int intB = 100;
        Integer integerA = 100;
        Integer integerB = 100;
        Integer integerC = new Integer(100);
        Integer integerD = new Integer(100);
        System.out.println("测试值为: " + intA);
        System.out.println("intA == intB结果为: " + (intA == intB));
        System.out.println("integerA.equals(intA)结果为: " + integerA.equals(intA) + ", integerA == intA结果为: " + (integerA == intA));
        System.out.println("integerA.equals(integerB)结果为: " + integerA.equals(integerB) + ", integerA == integerB结果为: " + (integerA == integerB));
        System.out.println("integerC.equals(intA)结果为: " + integerC.equals(intA) + ", integerC == intA结果为: " + (integerC == intA));
        System.out.println("integerA.equals(integerC)结果为: " + integerA.equals(integerC) + ", integerA == integerC结果为: " + (integerA == integerC));
        System.out.println("integerC.equals(integerD)结果为: " + integerC.equals(integerD) + ", integerC == integerD结果为: " + (integerC == integerD));
    }

    @Test
    public void test02() {
        int intA = 200;
        int intB = 200;
        Integer integerA = 200;
        Integer integerB = 200;
        Integer integerC = new Integer(200);
        Integer integerD = new Integer(200);
        System.out.println("测试值为: " + intA);
        System.out.println("intA == intB结果为: " + (intA == intB));
        System.out.println("integerA.equals(intA)结果为: " + integerA.equals(intA) + ", integerA == intA结果为: " + (integerA == intA));
        System.out.println("integerA.equals(integerB)结果为: " + integerA.equals(integerB) + ", integerA == integerB结果为: " + (integerA == integerB));
        System.out.println("integerC.equals(intA)结果为: " + integerC.equals(intA) + ", integerC == intA结果为: " + (integerC == intA));
        System.out.println("integerA.equals(integerC)结果为: " + integerA.equals(integerC) + ", integerA == integerC结果为: " + (integerA == integerC));
        System.out.println("integerC.equals(integerD)结果为: " + integerC.equals(integerD) + ", integerC == integerD结果为: " + (integerC == integerD));
    }

    @Test
    public void hexadecimalConversion() {
        int a = -17;
        System.out.println("二进制输出" + Integer.toBinaryString(a));
        System.out.println("八进制输出" + Integer.toOctalString(a));
        System.out.printf("八进制输出" + "%010o\n", a);
        //按10位十六进制输出，向右靠齐，左边用0补齐
        System.out.printf("十六进制输出" + "%010x\n", a);
        //按10位八进制输出，向右靠齐，左边用0补齐
        System.out.println("十六进制输出" + Integer.toHexString(a));
        System.out.println();

        int b = 17;
        System.out.println("二进制输出" + Integer.toBinaryString(b));
        System.out.println("八进制输出" + Integer.toOctalString(b));
        System.out.printf("八进制输出" + "%010o\n", b);
        System.out.printf("十六进制输出" + "%010x\n", b);
        System.out.println("十六进制输出" + Integer.toHexString(b));
    }

    @Test
    public void hexadecimalConversion01() {
        int a = -5;
        System.out.println(a);
        System.out.println("二进制输出" + Integer.toBinaryString(a));
        System.out.println(a + "<<2" + "=" + (a << 2));
        System.out.println("二进制输出" + Integer.toBinaryString(a << 2));
        System.out.println(a + ">>2" + "=" + (a >> 2));
        System.out.println("二进制输出" + Integer.toBinaryString(a >> 2));
        System.out.println("无符号右移" + a + ">>>2" + "=" + (a >>> 2));
        System.out.println("二进制输出" + Integer.toBinaryString(a >>> 2));

        int b = 5;
        System.out.println(b);
        System.out.println("二进制输出" + Integer.toBinaryString(b));
        System.out.println(b + "<<2" + "=" + (b << 2));
        System.out.println("二进制输出" + Integer.toBinaryString(b << 2));
        System.out.println(b + ">>2" + "=" + (b >> 2));
        System.out.println("二进制输出" + Integer.toBinaryString(b >> 2));
        System.out.println("无符号右移" + b + ">>>2" + "=" + (b >>> 2));
        System.out.println("二进制输出" + Integer.toBinaryString(b >>> 2));
    }
}
