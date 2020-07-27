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
}
