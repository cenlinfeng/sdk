package com.ddtsdk.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA 非对称加密算法，加解密工具类，
 * 加密长度 不能超过 128 个字节。
 */
public class RSAEncrypt {

    public static final String TAG = RSAEncrypt.class.getSimpleName() + " --> ";

    /**
     * 编码
     */
    public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    /**
     * 标准 jdk 加密填充方式，加解密算法/工作模式/填充方式
     */
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

    /**
     * RSA 加密算法
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * RSA 最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 随机生成 RSA 密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048，一般：1024
     */
    public static KeyPair getKeyPair(int keyLength) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            generator.initialize(keyLength);
            return generator.genKeyPair();
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    /**
     * 获取公钥 Base64 编码
     *
     * @param publicKey 公钥
     */
    public static String getPublicKeyBase64(PublicKey publicKey) {
        return DataUtils.base64Encode(publicKey.getEncoded());
    }

    /**
     * 获取私钥 Base64 编码
     *
     * @param privateKey 公钥
     */
    public static String getPrivateKeyBase64(PrivateKey privateKey) {
        return DataUtils.base64Encode(privateKey.getEncoded());
    }

    /**
     * 获取 PublicKey 对象
     *
     * @param pubKey 公钥，X509 格式
     */
    public static PublicKey getPublicKey(String pubKey) {
        try {
            // 将公钥进行 Base64 解码
            byte[] publicKey = DataUtils.base64Decode(pubKey);
            // 创建 PublicKey 对象并返回
            return KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(
                    new X509EncodedKeySpec(publicKey));
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    /**
     * 获取 PrivateKey 对象
     *
     * @param prvKey 私钥，PKCS8 格式
     */
    /*public static PrivateKey getPrivateKey(String prvKey) {
        try {
            // 私钥数据
            byte[] privateKey = DataUtils.base64Decode(prvKey);
            // 创建 PrivateKey 对象并返回
            return KeyFactory.getInstance(KEY_ALGORITHM)
                    .generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }*/

    /**
     * 从字符串中加载私钥
     *
     * @desc
     * @param privateKeyStr
     *            私钥字符串
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKeyStr)
            throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr, Base64.NO_WRAP);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            LogUtil.i("RSAUTIL", "loadPrivateKey: "+new String(buffer,"UTF-8"));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        }catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 处理异常
     */
    private static void handleException(Exception e) {
        e.printStackTrace();
        Log.e(TAG, TAG + e);
    }

    /**
     * 使用公钥将数据进行分段加密
     *
     * @param data   要加密的数据
     * @param pubKey 公钥 Base64 字符串，X509 格式
     * @return 加密后的 Base64 编码数据，加密失败返回 null
     */
    public static String encryptByPublicKey(String data, String pubKey) {
        try {
            byte[] bytes = data.getBytes(CHARSET_UTF8);
            // 创建 Cipher 对象
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            // 初始化 Cipher 对象，加密模式
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(pubKey));
            int inputLen = bytes.length;
            // 保存加密的数据
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0, i = 0;
            byte[] cache;
            // 使用 RSA 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
                }
                // 将加密以后的数据保存到内存
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            // 将加密后的数据转换成 Base64 字符串
            return DataUtils.base64Encode(encryptedData);
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    /**
     * 使用私钥将加密后的 Base64 字符串进行分段解密
     *
     * @param encryptBase64Data 加密后的 Base64 字符串
     * @param prvKey            私钥 Base64 字符串，PKCS8 格式
     * @return 解密后的明文，解密失败返回 null
     */
    public static String decryptByPrivateKey(String encryptBase64Data, String prvKey) {
        try {
            // 将要解密的数据，进行 Base64 解码
            byte[] encryptedData = DataUtils.base64Decode(encryptBase64Data);
            // 创建 Cipher 对象，用来解密
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            // 初始化 Cipher 对象，解密模式
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(prvKey));
            int inputLen = encryptedData.length;
            // 保存解密的数据
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0, i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                byte[] cache;
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                // 将解密后的数据保存到内存
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, CHARSET_UTF8);
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }
}