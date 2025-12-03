package com.smartplanner.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CryptoUtils Tests")
class CryptoUtilsTest {

  @Test
  @DisplayName("Should encrypt and decrypt string with default shift")
  void testEncryptDecryptDefault() {
    String original = "password123";
    String encrypted = CryptoUtils.encrypt(original);
    String decrypted = CryptoUtils.decrypt(encrypted);

    assertNotEquals(original, encrypted);
    assertEquals(original, decrypted);
  }

  @Test
  @DisplayName("Should encrypt and decrypt string with custom shift")
  void testEncryptDecryptCustomShift() {
    String original = "testuser";
    int shift = 10;
    String encrypted = CryptoUtils.encrypt(original, shift);
    String decrypted = CryptoUtils.decrypt(encrypted, shift);

    assertNotEquals(original, encrypted);
    assertEquals(original, decrypted);
  }

  @Test
  @DisplayName("Should handle empty string")
  void testEncryptEmptyString() {
    String original = "";
    String encrypted = CryptoUtils.encrypt(original);
    String decrypted = CryptoUtils.decrypt(encrypted);

    assertEquals("", encrypted);
    assertEquals("", decrypted);
  }

  @Test
  @DisplayName("Should handle special characters")
  void testEncryptSpecialCharacters() {
    String original = "user@123!";
    String encrypted = CryptoUtils.encrypt(original);
    String decrypted = CryptoUtils.decrypt(encrypted);

    assertEquals(original, decrypted);
  }

  @Test
  @DisplayName("Should handle lowercase letters")
  void testEncryptLowercase() {
    String original = "abcdefghijklmnopqrstuvwxyz";
    String encrypted = CryptoUtils.encrypt(original);
    String decrypted = CryptoUtils.decrypt(encrypted);

    assertEquals(original, decrypted);
  }

  @Test
  @DisplayName("Should handle uppercase letters")
  void testEncryptUppercase() {
    String original = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String encrypted = CryptoUtils.encrypt(original);
    String decrypted = CryptoUtils.decrypt(encrypted);

    assertEquals(original, decrypted);
  }

  @Test
  @DisplayName("Should handle numbers")
  void testEncryptNumbers() {
    String original = "0123456789";
    String encrypted = CryptoUtils.encrypt(original);
    String decrypted = CryptoUtils.decrypt(encrypted);

    assertEquals(original, decrypted);
  }

  @Test
  @DisplayName("Should handle long string")
  void testEncryptLongString() {
    String original = "This is a very long password with multiple words and spaces 123456!@#$%^&*()";
    String encrypted = CryptoUtils.encrypt(original);
    String decrypted = CryptoUtils.decrypt(encrypted);

    assertEquals(original, decrypted);
  }

  @Test
  @DisplayName("Should produce different encrypted strings for different shifts")
  void testDifferentShifts() {
    String original = "password";
    String encrypted1 = CryptoUtils.encrypt(original, 5);
    String encrypted2 = CryptoUtils.encrypt(original, 10);

    assertNotEquals(encrypted1, encrypted2);
  }

  @Test
  @DisplayName("Should handle zero shift")
  void testZeroShift() {
    String original = "password";
    String encrypted = CryptoUtils.encrypt(original, 0);

    assertEquals(original, encrypted);
  }

  @Test
  @DisplayName("Should handle negative shift")
  void testNegativeShift() {
    String original = "password";
    int shift = -10;
    String encrypted = CryptoUtils.encrypt(original, shift);
    String decrypted = CryptoUtils.decrypt(encrypted, shift);

    assertEquals(original, decrypted);
  }
}
