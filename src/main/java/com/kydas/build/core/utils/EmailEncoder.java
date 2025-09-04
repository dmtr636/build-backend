package com.kydas.build.core.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class EmailEncoder {
    @Value("${secret-key}")
    private String SECRET_KEY;

    public String generateCodeFromEmail(String email) throws Exception {
        String encryptedEmail = encryptEmail(email);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(encryptedEmail.getBytes());

        int code = Math.abs(new String(hash).hashCode()) % 10000;

        return String.format("%04d", code);
    }

    private String encryptEmail(String email) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encryptedBytes = cipher.doFinal(email.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
