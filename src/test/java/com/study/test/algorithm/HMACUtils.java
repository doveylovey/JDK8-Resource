package com.study.test.algorithm;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MAC(Message Authentication Code)，兼容了 MD 和 SHA 的特性，并且在它们的基础上加入了密钥。
 * 因此 MAC 也称为 HMAC(keyed-Hash Message Authentication Code)含有密钥的散列函数算法。
 * MD系列：HmacMD2、HmacMD4、HmacMD5
 * SHA系列：HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384、HmacSHA512
 * <p>
 * MAC 的算法的提供方如下(Commons Codec 中并没有提供 Hmac 算法的实现)：
 * 算法	        摘要长度	实现方
 * HmacMD2	    128	    Bouncy Castle
 * HmacMD4	    128	    Bouncy Castle
 * HmacMD5	    128	    JDK
 * HmacSHA1	    160	    JDK
 * HmacSHA224	224	    Bouncy Castle
 * HmacSHA256	256	    JDK
 * HmacSHA384	384	    JDK
 * HmacSHA512	512	    JDK
 * <p>
 * 例如：常用的 Linux 客户端 SecurityCRT。
 */
public class HMACUtils {
    /**
     * HmacMD2 加密：JDK 未提供
     */
    public static final String KEY_HMAC_MD2 = "HmacMD2";
    /**
     * HmacMD4 加密：JDK 未提供
     */
    public static final String KEY_HMAC_MD4 = "HmacMD4";
    /**
     * HmacMD5 加密
     */
    public static final String KEY_HMAC_MD5 = "HmacMD5";
    /**
     * HmacSHA1 加密
     */
    public static final String KEY_HMAC_SHA1 = "HmacSHA1";
    /**
     * HmacSHA224 加密：JDK 未提供
     */
    public static final String KEY_HMAC_SHA224 = "HmacSHA224";
    /**
     * HmacSHA384 加密
     */
    public static final String KEY_HMAC_SHA384 = "HmacSHA384";
    /**
     * HmacSHA256 加密
     */
    public static final String KEY_HMAC_SHA256 = "HmacSHA256";
    /**
     * HmacSHA512 加密
     */
    public static final String KEY_HMAC_SHA512 = "HmacSHA512";


    /**
     * HmacMD5 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] encodeByHmacMD5(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_HMAC_MD5);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();
        SecretKey restoreSecretKey = new SecretKeySpec(key, KEY_HMAC_MD5);
        Mac mac = Mac.getInstance(restoreSecretKey.getAlgorithm());
        mac.init(restoreSecretKey);
        return mac.doFinal(data);
    }

    /**
     * HmacSHA1 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] encodeByHmacSHA1(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_HMAC_SHA1);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();
        SecretKey restoreSecretKey = new SecretKeySpec(key, KEY_HMAC_SHA1);
        Mac mac = Mac.getInstance(restoreSecretKey.getAlgorithm());
        mac.init(restoreSecretKey);
        return mac.doFinal(data);
    }

    /**
     * HmacSHA256 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] encodeByHmacSHA256(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_HMAC_SHA256);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();
        SecretKey restoreSecretKey = new SecretKeySpec(key, KEY_HMAC_SHA256);
        Mac mac = Mac.getInstance(restoreSecretKey.getAlgorithm());
        mac.init(restoreSecretKey);
        return mac.doFinal(data);
    }

    /**
     * HmacSHA384 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] encodeByHmacSHA384(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_HMAC_SHA384);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();
        SecretKey restoreSecretKey = new SecretKeySpec(key, KEY_HMAC_SHA384);
        Mac mac = Mac.getInstance(restoreSecretKey.getAlgorithm());
        mac.init(restoreSecretKey);
        return mac.doFinal(data);
    }

    /**
     * HmacSHA512 加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] encodeByHmacSHA512(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_HMAC_SHA512);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();
        SecretKey restoreSecretKey = new SecretKeySpec(key, KEY_HMAC_SHA512);
        Mac mac = Mac.getInstance(restoreSecretKey.getAlgorithm());
        mac.init(restoreSecretKey);
        return mac.doFinal(data);
    }

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
        String src = "您好";
        System.out.println("源字符串：" + src);
        byte[] srcBytes = src.getBytes(StandardCharsets.UTF_8);

        byte[] hmacMD5 = encodeByHmacMD5(srcBytes);
        System.out.println("HmacMD5加密结果：" + Hex.encodeHexString(hmacMD5));

        byte[] hmacSHA1 = encodeByHmacSHA1(srcBytes);
        System.out.println("HmacSHA1加密结果：" + Hex.encodeHexString(hmacSHA1));

        byte[] hmacSHA256 = encodeByHmacSHA256(srcBytes);
        System.out.println("HmacSHA256加密结果：" + Hex.encodeHexString(hmacSHA256));

        byte[] hmacSHA384 = encodeByHmacSHA384(srcBytes);
        System.out.println("HmacSHA384加密结果：" + Hex.encodeHexString(hmacSHA384));

        byte[] hmacSHA512 = encodeByHmacSHA512(srcBytes);
        System.out.println("HmacSHA512加密结果：" + Hex.encodeHexString(hmacSHA512));
    }
}
