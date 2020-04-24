package com.study.test;

import org.junit.Test;

/**
 * Java 中的 char 究竟能存中文吗？
 * char 型变量是用来存储 unicode 编码的字符的，unicode 编码字符集中包含了汉字，所以 char 型变量中可以存储汉字。
 * 但如果某个特殊的汉字没有被包含在 unicode 编码字符集中，那么 char 型变量中就不能存储这个特殊汉字。
 * 说明：unicode 编码固定占用两个字节，所以 char 类型的变量也是占用两个字节。
 * <p>
 * Unicode（统一码、万国码、单一码）是一种在计算机上使用的字符编码，它为每种语言中的每个字符设定了统一并且唯一的二进制编码，
 * 以满足跨语言、跨平台进行文本转换、处理的要求。传统的编码方式存在的缺陷：
 * ①在不同的编码方案下有可能对应不同的字母；
 * ②采用大字符集的语言其编码长度可能不同。
 * <p>
 * 目前的 Unicode 版本对应于 UCS-2，使用16位的编码空间，也就是每个字符占用2个字节。不同的看编码占据字节数也不同：
 * utf-32码的中文是4字节；
 * utf-8码的中文是3字节，字母是1字节，因为utf-8是变长编码；
 * 而 gbk/gbk18030 码的中文是2字节的，英文是1个字节。
 */
public class CharTests {
    @Test
    public void test01() {
        char ch = '中';
        System.out.println("字符为：" + ch);
        int max = Character.MAX_VALUE;
        int min = Character.MIN_VALUE;
        // 0 < char < 65535
        System.out.println(min + " < char < " + max);
    }
}
