package com.welab.k8sbackenduser.secret.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecureHashUtils {
    public static String hash(String message) {
        // TODO SHA-256으로 해시화 하기
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = digest.digest(message.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean matches(String message, String hashedMessage){
        String hashed = hash(message);

        return hashed.equals(hashedMessage);
    }
}
