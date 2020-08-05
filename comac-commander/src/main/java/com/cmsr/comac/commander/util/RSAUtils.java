package com.cmsr.comac.commander.util;

import com.google.common.io.Files;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.File;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author ZQ
 * @create 2020-07-01
 */
public class RSAUtils {
    public static BASE64Encoder base64Encoder = new BASE64Encoder();
    public static BASE64Decoder base64Decoder = new BASE64Decoder();

    //获取Base64加密后得公钥
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = Base64.getMimeDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    //获取Base64加密后的私钥
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes =Base64.getMimeDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    // 公钥加密
    public static String encryptByPublicKey(String content, String publicKeyPath) throws Exception {
        byte[] bytes = Files.toByteArray(new File(publicKeyPath));
        String s = new String(bytes);
        String publicKeyStr = s.trim();
        System.out.println("publicKeyStr==============================" + publicKeyStr);
        // 获取公钥
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(content.getBytes());
        String cipherStr = base64Encoder.encode(cipherText);
        return cipherStr;
    }

    // 私钥解密
    public static String decryptByPrivateKey(String content, String privateKeyPath) throws Exception {
        byte[] bytes = Files.toByteArray(new File(privateKeyPath));
        String s = new String(bytes);
        String privateKeyStr = s.trim();
        System.out.println("privateKeyStr==============================" + privateKeyStr);
        // 获取私钥
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cipherText = base64Decoder.decodeBuffer(content);
        byte[] decryptText = cipher.doFinal(cipherText);
        return new String(decryptText);
    }
}
