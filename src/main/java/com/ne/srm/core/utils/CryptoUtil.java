package com.ne.srm.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CryptoUtil {


    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    /**
     * SHA-256 哈希
     *
     * @param text 原文
     * @return 64位十六进制字符串
     */
    public static String sha256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(
                    text.getBytes(StandardCharsets.UTF_8)
            );

            return toHex(hash);

        } catch (Exception e) {
            throw new RuntimeException("SHA256 error", e);
        }
    }

    /**
     * byte数组转十六进制
     */
    private static String toHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i] & 0xff;

            chars[i * 2] = HEX_ARRAY[b >>> 4];
            chars[i * 2 + 1] = HEX_ARRAY[b & 15];
        }
        return new String(chars);
    }
}
