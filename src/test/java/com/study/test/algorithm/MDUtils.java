package com.study.test.algorithm;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要算法主要分为3类：MD(Message Digest)、SHA(Secure Hash Algorithm)、MAC(Message Authentication Code)，
 * 这3类算法的主要作用是验证数据的完整性，是数字签名的核心算法。
 * <p>
 * MD 算法家族有3类：MD2、MD4、MD5，且 MD 家族生成的都是128位的信息摘要。
 * 算法	摘要长度	实现方
 * MD2	128	    JDK
 * MD4	128	    Bouncy Castle
 * MD5	128	    JDK
 * 信息摘要算法由于使用的是一种单向函数，所以理论上是不可破解的(山东大学的王晓云教授已经破解了MD5和SHA，所以消息摘要是可以伪造的，只不过难度比较大)。
 * 所有MD算法进行摘要的结果都是128为位（32位16进制的数字，因为1位16进制数代表4位二进制数）。
 * <p>
 * Bouncy Castle 提供了 MD2、MD4、MD5 的实现，对消息摘要算法的支持比较完善，但其 API 还是没有 Apache 的 Commons Codec 友善。因此，如果我们要进行 MD2 和 MD4 实现，最好选用 Commons Codec。
 */
public class MDUtils {
    /**
     * MD2 加密
     */
    public static final String KEY_MD2 = "MD2";
    /**
     * MD4 加密：JDK 未提供
     */
    public static final String KEY_MD4 = "MD4";
    /**
     * MD5 加密
     */
    public static final String KEY_MD5 = "MD5";

    /**
     * MD2 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encodeByMD2(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md2 = MessageDigest.getInstance(KEY_MD2);
        // return md2.digest(data);
        md2.update(data);
        return md2.digest();
    }

    /**
     * MD5 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encodeByMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        // return md5.digest(data);
        md5.update(data);
        return md5.digest();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String src = "您好";
        System.out.println("源字符串：" + src);
        byte[] srcBytes = src.getBytes(StandardCharsets.UTF_8);

        byte[] md2 = encodeByMD2(srcBytes);
        System.out.println("MD2加密结果：" + Hex.encodeHexString(md2));

        byte[] md5 = encodeByMD5(srcBytes);
        System.out.println("MD5加密结果：" + Hex.encodeHexString(md5));
    }
}
