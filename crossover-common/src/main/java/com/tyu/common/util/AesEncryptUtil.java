package com.tyu.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class AesEncryptUtil {
    private static final String encode = "UTF-8";
    private static final String mode = "AES/CBC/PKCS5Padding";

    /**
     * JDK只支持AES-128加密，也就是密钥长度必须是128bit；参数为密钥key，key的长度小于16字符时用"0"补充，key长度大于16字符时截取前16位
     **/
    private static SecretKeySpec get128BitsKey(String key) {
        if (key == null) {
            key = "";
        }
        byte[] data = null;
        StringBuffer buffer = new StringBuffer(16);
        buffer.append(key);
        //小于16后面补0
        while (buffer.length() < 16) {
            buffer.append("0");
        }
        //大于16，截取前16个字符
        if (buffer.length() > 16) {
            buffer.setLength(16);
        }
        try {
            data = buffer.toString().getBytes(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
    }

    /**
     * 创建128位的偏移量，iv的长度小于16时后面补0，大于16，截取前16个字符;
     *
     * @param iv
     * @return
     */
    private static IvParameterSpec get128BitsIV(String iv) {
        if (iv == null) {
            iv = "";
        }
        byte[] data = null;
        StringBuffer buffer = new StringBuffer(16);
        buffer.append(iv);
        while (buffer.length() < 16) {
            buffer.append("0");
        }
        if (buffer.length() > 16) {
            buffer.setLength(16);
        }
        try {
            data = buffer.toString().getBytes(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }

    /**
     * 填充方式为Pkcs5Padding的加密函数
     * 填充方式为Pkcs5Padding时，最后一个块需要填充χ个字节，填充的值就是χ，也就是填充内容由JDK确定
     *
     * @param srcContent：  明文
     * @param password：   加密密钥（不足128bits时，填"0"补足）
     * @param iv:           初始向量（不足128bits时，填"0"补足）
     *
     * @return 密文(16进制表示)
     *
     */
    public static String encrypt(String srcContent, String password, String iv) {
        SecretKeySpec key = get128BitsKey(password);
        IvParameterSpec ivParameterSpec = get128BitsIV(iv);
        try {
            Cipher cipher = Cipher.getInstance(mode);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            byte[] byteContent = srcContent.getBytes(encode);
            byte[] encryptedContent = cipher.doFinal(byteContent);
            String result = HexUtil.byte2hex(encryptedContent);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 填充方式为Pkcs5Padding的解密函数
     * 填充方式为Pkcs5Padding时，最后一个块需要填充χ个字节，填充的值就是χ，也就是填充内容由JDK确定
     *
     * @param encryptedContent：  密文
     * @param password：          加密密钥（不足128bits时，填"0"补足）
     * @param iv:                 初始向量（不足128bits时，填"0"补足）
     *
     * @return 密文(16进制表示)
     *
     */
    public static String decrypt(String encryptedContent, String password, String iv) {
        SecretKeySpec key = get128BitsKey(password);
        IvParameterSpec ivParameterSpec = get128BitsIV(iv);
        try {
            byte[] content = HexUtil.hex2byte(encryptedContent);
            Cipher cipher = Cipher.getInstance(mode);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            byte[] decryptedContent = cipher.doFinal(content);
            String result = new String(decryptedContent);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}