package com.xosmos.utils;

import org.springframework.util.DigestUtils;

public class EncryptUtils {
    public static String encrypt(String plaintext) {
        return DigestUtils.md5DigestAsHex(plaintext.getBytes());
    }
}
