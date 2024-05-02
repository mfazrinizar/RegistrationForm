package com.github.mfazrinizar.RegistrationForm.Util;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : PasswordHasher.java
 */

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    // Hashing
    public String getHashedPassword(String passwordToHash, byte[] salt) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    // Salt
    public byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    // Encode salt (byte[] to String)
    public String encodeToBase64(byte[] salt) {
        return Base64.getEncoder().encodeToString(salt);
    }

    // Decode url-safe base64 (String Base64 to String UTF-8)
    public String urlBase64Decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString), StandardCharsets.UTF_8);
    }

    // Decode base64 (String Base64 to byte[])
    public byte[] base64Decode(String encodedString) {
        return Base64.getDecoder().decode(encodedString);
    }
}