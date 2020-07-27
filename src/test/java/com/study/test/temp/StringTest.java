package com.study.test.temp;

import org.junit.Test;

/**
 * @Description: String 类的测试用例
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
        // false：str1 在堆中创建对象，str2 在常量池中创建对象
        System.out.println(str1 == str2);
        // true：str2 在常量池中创建对象，str3 直接返回的 str2 创建的对象的引用，所以 str2 和 str3 指向常量池中同一个对象
        System.out.println(str2 == str3);
        // true：str4 返回常量池中值为"123"的对象，因此 str4 和 str2、str3 都相等
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
        // X 为常量，值是固定的，因此 X+"DEF" 值已经定下来为 ABCDEF，实际上编译后得代码相当于 String str8 = "ABCDEF"
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

    @Test
    public void test03() {
        int sc = 0;
        int DEFAULT_CAPACITY = 16;
        int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
        /**
         * >>：带符号右移(相当于除以2)。正数右移高位补0，负数右移高位补1。例：
         * 4 >> 1：4的原码为 0000 0000 0000 0000 0000 0000 0000 0100，右移一位后为 0000 0000 0000 0000 0000 0000 0000 0010，转成十进制为 2。
         * -4 >> 1：-4的原码为 1000 0000 0000 0000 0000 0000 0000 0100，补码为 1111 1111 1111 1111 1111 1111 1111 1100，右移一位后为 1111 1111 1111 1111 1111 1111 1111 1110，再转成原码为 1000 0000 0000 0000 0000 0000 0000 0010，转成十进制为 -2。
         * <p>
         * >>>：无符号右移。无论是正数还是负数，高位通通补0。例：
         * 4 >>> 1：4的原码为 0000 0000 0000 0000 0000 0000 0000 0100，右移一位后为 0000 0000 0000 0000 0000 0000 0000 0010，转成十进制为 2。
         * -4 >>> 1：-4的原码为 1000 0000 0000 0000 0000 0000 0000 0100，补码为 1111 1111 1111 1111 1111 1111 1111 1100，右移一位后为 0111 1111 1111 1111 1111 1111 1111 1110，由于右移后符号位为 1，即变成正数，所以原码与补码一样为 0111 1111 1111 1111 1111 1111 1111 1110，转成十进制为 2147483646。
         */
        sc = n - (n >>> 2);
        System.out.println(sc);
    }
}
