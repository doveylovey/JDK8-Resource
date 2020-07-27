package com.study.test.temp;

import org.junit.Assert;
import org.junit.Test;

/**
 * 逻辑运算符测试类
 */
public class LogicalOperatorTests {
    @Test
    public  void test01() {
        System.out.println("2 << 3 结果为：" + (2 << 3));
        Assert.assertEquals(2 << 3,16);
        System.out.println("3 << 2 结果为：" + (3 << 2));
        Assert.assertEquals(3 << 2,12);
        System.out.println("~3 结果为：" + (~3));
        Assert.assertEquals(~3,-4);
    }
}
