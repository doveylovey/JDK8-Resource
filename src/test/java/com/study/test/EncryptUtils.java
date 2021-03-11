package com.study.test;

import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * 加解密工具类
 */
public class EncryptUtils {
    private static final byte[] DES_KEY = new byte[]{-83, 107, -125, 67, -57, 76, -85, 112};

    private EncryptUtils() {
        throw new AssertionError("No com.study.test.EncryptUtils instances for you!");
    }

    public static String decryptBasedDes(String cryptData) {
        String decryptedData = null;
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, key, sr);
            decryptedData = new String(cipher.doFinal((new BASE64Decoder()).decodeBuffer(cryptData)));
            return decryptedData;
        } catch (Exception var7) {
            throw new RuntimeException("解密错误，错误信息：", var7);
        }
    }

    public static String encryptBasedDes(String data) {
        String encryptedData = null;
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, key, sr);
            encryptedData = (new BASE64Encoder()).encode(cipher.doFinal(data.getBytes()));
            return encryptedData;
        } catch (Exception var7) {
            throw new RuntimeException("加密错误，错误信息：", var7);
        }
    }

    public static String dencryptDES(String text, String secret) {
        String decryptedData = null;
        boolean validate = calSecretLength(secret);
        if (!validate) {
            throw new RuntimeException("密钥不合法，密钥必须是8的倍数");
        } else {
            try {
                SecureRandom sr = new SecureRandom();
                DESKeySpec deskey = new DESKeySpec(secret.getBytes());
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey key = keyFactory.generateSecret(deskey);
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(2, key, sr);
                decryptedData = new String(cipher.doFinal(decodeBase64(text)));
                return decryptedData;
            } catch (Exception var9) {
                throw new RuntimeException("解密错误，错误信息：", var9);
            }
        }
    }

    public static String encryptDES(String text, String secret) {
        String encryptedData = null;
        boolean validate = calSecretLength(secret);
        if (!validate) {
            throw new RuntimeException("密钥不合法，密钥必须是8的倍数");
        } else {
            try {
                SecureRandom sr = new SecureRandom();
                DESKeySpec deskey = new DESKeySpec(secret.getBytes());
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey key = keyFactory.generateSecret(deskey);
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(1, key, sr);
                encryptedData = encodeBase64(cipher.doFinal(text.getBytes()));
                return encryptedData;
            } catch (Exception var9) {
                throw new RuntimeException("加密错误，错误信息：", var9);
            }
        }
    }

    private static boolean calSecretLength(String secret) {
        if (secret == null || secret.trim().length() == 0) {
            return false;
        } else {
            byte[] secretByte = secret.getBytes();
            int overplus = secretByte.length % 8;
            return overplus == 0;
        }
    }

    public static String encodeBase64(byte[] encodeStr) {
        return (new BASE64Encoder()).encode(encodeStr);
    }

    public static byte[] decodeBase64(String decodeStr) throws IOException {
        return (new BASE64Decoder()).decodeBuffer(decodeStr);
    }

    public static String md5Hex(String text) {
        return DigestUtils.md5Hex(getContentBytes(text, "UTF-8"));
    }

    public static String md5Hex(String text, String charset) {
        return DigestUtils.md5Hex(getContentBytes(text, charset));
    }

    private static byte[] getContentBytes(String content, String charset) {
        if (charset != null && !"".equals(charset)) {
            try {
                return content.getBytes(charset);
            } catch (UnsupportedEncodingException var3) {
                throw new RuntimeException("MD5 签名过程中出现错误，指定的编码集不对，您目前指定的编码集是：" + charset);
            }
        } else {
            return content.getBytes();
        }
    }
}
