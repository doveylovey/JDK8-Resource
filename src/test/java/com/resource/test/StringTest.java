package com.resource.test;

import org.junit.Test;

/**
 * @Description: String类的测试用例
 * @Author: TeGongX
 * @Email: 1135782208@qq.com
 * @Date: 2019年10月13日
 * @Version: V1.0.0
 * @Copyright: Copyright (c) 2019
 */
public class StringTest {
    @Test
    public void test01() {
        String str1 = new String("123");
        String str2 = "123";
        String str3 = "123";
        String str4 = str1.intern();
        // false：str1在堆中创建对象，str2在常量池中创建对象
        System.out.println(str1 == str2);
        // true：str2在常量池中创建对象，str3直接返回的str2创建的对象的引用，所以str2和str3指向常量池中同一个对象
        System.out.println(str2 == str3);
        // true：str4返回常量池中值为"123"的对象，因此str4和str2、str3都相等
        System.out.println(str4 == str3);
    }

    public static final String X = "ABC";

    @Test
    public void test02() {
        String str5 = new String("ABC");
        // 堆中创建
        String str6 = str5 + "DEF";
        // 常量池
        String str7 = "ABC" + "DEF";
        // X为常量,值是固定的,因此X+"DEF"值已经定下来为ABCDEF，实际上编译后得代码相当于String str8 = "ABCDEF"
        String str8 = X + "DEF";
        String str9 = "ABC";
        // 堆中
        String str10 = str9 + "DEF";
        // false
        System.out.println(str6 == str7);
        // true
        System.out.println(str8 == str7);
        // false
        System.out.println(str10 == str7);
        // true
        System.out.println(X == str9);
    }
}
