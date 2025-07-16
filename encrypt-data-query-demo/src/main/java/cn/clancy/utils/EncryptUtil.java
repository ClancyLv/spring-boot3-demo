package cn.clancy.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * @Author ClancyLv
 * @Date 2025/7/15 17:55
 * @Description 工具类--加密
 */
public class EncryptUtil {
    /**
     * AES算法使用的秘钥，用于对数据进行加密和解密操作。
     * 请妥善保管此秘钥，避免泄露导致安全风险。
     */
    private final static String AES_KEY = "MacOARmxfUkkgRreWCTIUEsH06ZPxDZa";

    /**
     * HMAC-SHA256算法使用的秘钥，用于对数据进行哈希消息认证。
     * 请妥善保管此秘钥，避免泄露导致安全风险。
     */
    private final static String HMAC_SHA256_KEY = "8f865828-d835-4f3a-8eb7-9a06dcb6c269";

    public static void main(String[] args) {
        System.out.println(AES_KEY);
    }


    /**
     * 使用AES算法对数据进行加密。
     *
     * @param plaintext 明文。
     * @return 加密后的Base64编码字符串。
     */
    public static String aesEncrypt(String plaintext) {
        if (StrUtil.isBlank(plaintext)) {
            throw new IllegalArgumentException("明文不能为空");
        }
        // 加密
        return SecureUtil.aes(AES_KEY.getBytes()).encryptBase64(plaintext, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 使用AES算法对加密后的数据进行解密。
     *
     * @param ciphertext 密文。
     * @return 解密后的字符串数据。
     */
    public static String aesDecrypt(String ciphertext) {
        if (StrUtil.isBlank(ciphertext)) {
            throw new IllegalArgumentException("密文不能为空");
        }
        // 解密
        return SecureUtil.aes(AES_KEY.getBytes()).decryptStr(ciphertext, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 使用HMAC-SHA256算法对提供的数据进行加密，并返回Base64编码后的字符串。
     *
     * @param data 要进行加密的原始数据。
     * @return 加密后的Base64编码字符串。
     */
    public static String hmac(String data) {
        if (StrUtil.isBlank(data)) {
            throw new IllegalArgumentException("被摘要数据不能为空");
        }
        return SecureUtil.hmacSha256(HMAC_SHA256_KEY.getBytes()).digestBase64(data, CharsetUtil.CHARSET_UTF_8, true);
    }
}
