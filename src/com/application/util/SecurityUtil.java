package com.application.util;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtil {

    private static final char[] HEX = new char[] { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static String DECODE_MODE = "AES";
    private static String ENCRYPT_KEY = "SABER74189632DYL";
    private static String ENCRYPT_IV = "6543210987654321";

    public SecurityUtil() {

    }

    /**
     * 加密
     */
    public String enCrypt(String source) {
        System.out.println("password :" + source);
        String text = "";
        SecretKeySpec key;
        try {
            key = new SecretKeySpec(ENCRYPT_KEY.getBytes("UTF-8"), DECODE_MODE);
            IvParameterSpec iv = new IvParameterSpec(
                    ENCRYPT_IV.getBytes("UTF-8"));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] result = cipher.doFinal(source.getBytes());

            // java8 base64util
            Encoder encoder = Base64.getMimeEncoder();
            text = encoder.encodeToString(result);

            System.out.println("暗号化RESULT:" + text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 解密
     */
    public String deCrypt(String enCrypt) {
        String result = "";
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] encrypt = decoder.decode(enCrypt);

            SecretKeySpec key = new SecretKeySpec(ENCRYPT_KEY.getBytes("UTF-8"),
                    DECODE_MODE);
            IvParameterSpec iv = new IvParameterSpec(
                    ENCRYPT_IV.getBytes("UTF-8"));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            byte[] output = cipher.doFinal(encrypt);

            result = new String(output);
            System.out.println("解密RESULT:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * hash
     */
    public String getDigest(String source, String securityMode)
            throws Throwable {
        MessageDigest md = MessageDigest.getInstance(securityMode);
        md.update(source.getBytes());
        byte[] buff = md.digest();
        char[] ch = new char[buff.length * 2];
        for (int i = 0, offest = 0; i < buff.length; i++) {
            ch[offest++] = HEX[buff[i] >>> 4 & 0xf];
            ch[offest++] = HEX[buff[i] & 0xf];
        }
        String hashResult = new String(ch);
        System.out.println(hashResult);
        return hashResult;
    }

}
