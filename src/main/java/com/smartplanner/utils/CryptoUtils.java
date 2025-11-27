package com.smartplanner.utils;

/**
 * Utility class for encryption and decryption operations.
 * Uses a simple Caesar cipher for data obfuscation.
 * 
 * Source: https://github.com/thomascoe/java-encryption/blob/master/CaesarCipher.java
 */
public class CryptoUtils {
    
    private static final int DEFAULT_SHIFT = 101;
    
    /**
     * Encrypts a string using Caesar cipher.
     * 
     * @param original the original string to encrypt
     * @param shift the shift value for encryption
     * @return the encrypted string
     */
    public static String encrypt(String original, int shift) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            int c = original.charAt(i) + shift;
            if (c > 126) {
                c -= 95;
            } else if (c < 32) {
                c += 95;
            }
            encrypted.append((char) c);
        }
        return encrypted.toString();
    }
    
    /**
     * Decrypts a string using Caesar cipher.
     * 
     * @param encrypted the encrypted string to decrypt
     * @param shift the shift value for decryption
     * @return the decrypted string
     */
    public static String decrypt(String encrypted, int shift) {
        return encrypt(encrypted, -shift);
    }
    
    /**
     * Encrypts a string using the default shift value.
     * 
     * @param original the original string to encrypt
     * @return the encrypted string
     */
    public static String encrypt(String original) {
        return encrypt(original, DEFAULT_SHIFT);
    }
    
    /**
     * Decrypts a string using the default shift value.
     * 
     * @param encrypted the encrypted string to decrypt
     * @return the decrypted string
     */
    public static String decrypt(String encrypted) {
        return decrypt(encrypted, DEFAULT_SHIFT);
    }
}