package com.study.test.algorithm;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Base64 算法是基于64个字符的一种替换算法。
 * Base64 加密的产生是电子邮件的历史问题：邮件只能传输 ASCII 码。
 * Base64 加密的应用场景：email、密钥、证书文件。
 * Base64 算法目前有3种实现方式：JDK、Bouncy Castle、Commons Codec。
 */
public class Base64Utils {
    /**
     * BASE64 加密
     *
     * @param data
     * @return
     */
    public static String encode(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encodeBuffer(data);
    }

    /**
     * BASE64 解密
     *
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] decode(String data) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);
    }

    public static void main(String[] args) throws IOException {
        String src = "您好";
        System.out.println("源字符串：" + src);
        byte[] srcBytes = src.getBytes(StandardCharsets.UTF_8);

        String encode = encode(srcBytes);
        System.out.println("Base64加密结果：" + encode);

        byte[] decode = decode(encode);
        System.out.println("Base64解密结果：" + new String(decode, StandardCharsets.UTF_8));
    }
}
