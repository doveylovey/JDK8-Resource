package com.study.test.string;

import org.junit.Test;

/**
 * String 类的测试用例
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年01月20日
 */
public class StringTest02 {
    @Test
    public void test01() {
        /**
         * 面试题：String s1 = new String("abc");这句话创建了几个对象？答案是两个，原因如下：
         * 先有字符串 "abc" 放入常量池，然后 new 了一份字符串 "abc" 放入 Java 堆(字符串常量 "abc" 在编译期就已经确定放入常量池，
         * 而 Java 堆上的 "abc" 是在运行期初始化阶段才确定)，然后 Java 栈的 s1 指向 Java 堆上的 "abc"。
         */
        // 堆内存的地址值
        String s1 = new String("abc");
        String s2 = "abc";
        // 输出false,因为一个是堆内存，一个是常量池的内存，故两者是不同的。
        System.out.println(s1 == s2);
        // 输出true
        System.out.println(s1.equals(s2));
    }

    @Test
    public void test02() {
        // java 中 String s = new String("abc") 创建了几个对象？答案是两个。
        // 首先要明白两个概念：引用变量、对象。对象一般通过 new 在堆中创建，而 s 只是一个引用变量。
        // 所有的字符串都是 String 对象，由于字符串文字的大量使用，java 中为了节省时间，在编译阶段会把字符串文字放在常量池中，
        // 常量池的一个好处就是可以把相同的字符串合并，占用一个空间，可以用 == 判断两个引用变量是否指向了一个地址(即一个对象)。
        // String s = new String("abc") 实际上 "abc" 本身就是常量池中的一个对象，在运行 new String() 时，
        // 把常量池中的字符串 "abc" 复制到堆中，并把这个对象的引用交给 s，所以创建了两个 String 对象，一个在常量池中，一个在堆中。
        String s1 = "abc";
        String s2 = "abc";
        // 输出 true：指向了同一个对象，即常量池中保存了一个对象
        System.out.println(s1 == s2);
    }

    @Test
    public void test03() {
        String s1 = new String("abc");
        String s2 = new String("abc");
        // 输出 false：在堆中创建了两个对象，但是在文字池中有一个对象，所以共创建了三个对象。
        System.out.println(s1 == s2);
    }

    @Test
    public void test04() {
        // String s = new String("abc") 并不是给 s 赋值，而是把字符串 "abc" 的引用交给 s 持有
        String s = new String("abc");
        // 再把 s 指向字符串 "cba"，导致 "cba" 的引用地址把 "abc" 的引用地址覆盖，所以输出结果为 cba
        s = "cba";
        System.out.println(s);
    }
}
