package com.study.test.algorithm;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要算法主要分为3类：MD(Message Digest)、SHA(Secure Hash Algorithm)、MAC(Message Authentication Code)，
 * 这3类算法的主要作用是验证数据的完整性，是数字签名的核心算法。
 * <p>
 * SHA安全散列算法，固定长度的摘要信息。被认为是 MD5 的继承者。是一个系列，包括 SHA-1、SHA-2(如：SHA-224、SHA-256、SHA-384、SHA-512)，
 * 也就是除了 SHA-1，其他的4种都被称为是 SHA-2，每种算法的摘要长度和实现方如下：
 * 算法	    摘要长度	实现方
 * SHA-1	160	    JDK
 * SHA-224	224	    Bouncy Castle
 * SHA-256	256	    JDK
 * SHA-384	384	    JDK
 * SHA-512	512	    JDK
 * <p>
 * SHA 算法的实现和 MD 算法的实现大同小异，也是 JDK 提供了默认的几种实现，Apache 的 Commons Codec 在 JDK 的基础上进行了优化，
 * 使其更好用，而 Bouncy Castle 是 JDK 的拓展，提供了 JDK 和 Commons Codec 没有的 SHA-224 的实现。
 */
public class SHAUtils {
    /**
     * SHA-1 加密
     */
    public static final String KEY_SHA1 = "SHA";
    /**
     * SHA-224 加密：JDK 未提供
     */
    public static final String KEY_SHA224 = "SHA-224";
    /**
     * SHA-256 加密
     */
    public static final String KEY_SHA256 = "SHA-256";
    /**
     * SHA-384 加密
     */
    public static final String KEY_SHA384 = "SHA-384";
    /**
     * SHA-512 加密
     */
    public static final String KEY_SHA512 = "SHA-512";

    /**
     * SHA 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encodeBySHA1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA1);
        // return sha.digest(data);
        sha.update(data);
        return sha.digest();
    }

    /**
     * SHA-256 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encodeBySHA256(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA256);
        // return sha.digest(data);
        sha.update(data);
        return sha.digest();
    }

    /**
     * SHA-384 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encodeBySHA384(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA384);
        // return sha.digest(data);
        sha.update(data);
        return sha.digest();
    }

    /**
     * SHA-512 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encodeBySHA512(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA512);
        // return sha.digest(data);
        sha.update(data);
        return sha.digest();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String src = "您好";
        System.out.println("源字符串：" + src);
        byte[] srcBytes = src.getBytes(StandardCharsets.UTF_8);

        byte[] sha1 = encodeBySHA1(srcBytes);
        System.out.println("SHA-1加密：" + Hex.encodeHexString(sha1));

        byte[] sha256 = encodeBySHA256(srcBytes);
        System.out.println("SHA-256加密：" + Hex.encodeHexString(sha256));

        byte[] sha384 = encodeBySHA384(srcBytes);
        System.out.println("SHA-384加密：" + Hex.encodeHexString(sha384));

        byte[] sha512 = encodeBySHA512(srcBytes);
        System.out.println("SHA-512加密：" + Hex.encodeHexString(sha512));
    }
}
