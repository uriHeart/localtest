package com.argo.restapi.util;

import com.argo.restapi.exception.HashingFailException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashUtil {

    public static String sha256(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(msg.getBytes());
            return byteToHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        throw new HashingFailException("sha256 hash fail");
    }

    public static String byteToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
