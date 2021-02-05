package com.study.test.net;

import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * String 类的测试用例
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年1月29日
 */
public class UrlEncoderDecoderTest01 {
    @Test
    public void testURLcode() throws Exception {
        System.out.println(StandardCharsets.UTF_8.name());

        // URLEncoder.encode() 方法用于将普通字符串转换成 application/x-www-form-urlencoded 字符串
        // 采用utf-8字符集
        System.out.println(URLEncoder.encode("北京大学", "UTF-8"));
        // 采用GBK字符集
        System.out.println(URLEncoder.encode("北京大学", "GBK"));

        // URLDecoder.decode() 方法用于将 application/x-www-form-urlencoded 字符串转换成普通字符串
        // 采用UTF-8字符集进行解码
        System.out.println(URLDecoder.decode("%E5%8C%97%E4%BA%AC%E5%A4%A7%E5%AD%A6", "UTF-8"));
        // 采用GBK字符集进行解码
        System.out.println(URLDecoder.decode("%B1%B1%BE%A9%B4%F3%D1%A7", "GBK"));
    }
}
