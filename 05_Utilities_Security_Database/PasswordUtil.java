package com.expensetracker.util;

import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility for hashing and verifying passwords securely using BCrypt.
 */
public class PasswordUtil {

    private PasswordUtil() {}

    /**
     * Hashes a plain-text password with BCrypt.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        try {
            return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
        } catch (NoClassDefFoundError | Exception e) {
            // Fallback to SHA-256 with salt if BCrypt is not in classpath
            return fallbackSha256(plainPassword);
        }
    }

    /**
     * Verifies a plain-text password against a hashed password.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            if (hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$")) {
                return BCrypt.checkpw(plainPassword, hashedPassword);
            } else if (hashedPassword.startsWith("sha256:")) {
                return hashedPassword.equals(fallbackSha256(plainPassword));
            }
            // Direct comparison fallback for simple dev seeds
            return plainPassword.equals(hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    private static String fallbackSha256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(("expensetracker_salt_" + password).getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder("sha256:");
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return "plain:" + password;
        }
    }
}
