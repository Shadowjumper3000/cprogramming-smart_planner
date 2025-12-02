package com.smartplanner.auth;

import com.smartplanner.utils.CryptoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuthenticationManager Tests")
class AuthenticationManagerTest {

  private File loginFile;

  @BeforeEach
  void setUp() throws IOException {
    // Use actual data directory since AuthenticationManager uses hardcoded path
    File dataDir = new File("data");
    if (!dataDir.exists()) {
      dataDir.mkdirs();
    }

    loginFile = new File(dataDir, "login_info.csv");
    // Clear the file for testing
    try (java.io.FileWriter writer = new java.io.FileWriter(loginFile, false)) {
      writer.write("");
    }
  }

  @Test
  @DisplayName("Should initialize with empty login info when file is empty")
  void testInitializeEmptyFile() {
    AuthenticationManager authManager = new AuthenticationManager();
    HashMap<String, String> loginInfo = authManager.getLoginInfo();

    assertNotNull(loginInfo);
    assertTrue(loginInfo.isEmpty());
  }

  @Test
  @DisplayName("Should load encrypted credentials from file")
  void testLoadEncryptedCredentials() throws IOException {
    String username = "testuser";
    String password = "password123";
    String encryptedUsername = CryptoUtils.encrypt(username);
    String encryptedPassword = CryptoUtils.encrypt(password);

    // Write encrypted credentials to file
    try (FileWriter writer = new FileWriter(loginFile)) {
      writer.write(encryptedUsername + "," + encryptedPassword);
    }

    AuthenticationManager authManager = new AuthenticationManager();
    HashMap<String, String> loginInfo = authManager.getLoginInfo();

    assertEquals(1, loginInfo.size());
    assertTrue(loginInfo.containsKey(username));
    assertEquals(password, loginInfo.get(username));
  }

  @Test
  @DisplayName("Should validate correct credentials")
  void testValidateCorrectCredentials() throws IOException {
    String username = "user";
    String password = "pass";

    // Write encrypted credentials
    try (FileWriter writer = new FileWriter(loginFile)) {
      writer.write(CryptoUtils.encrypt(username) + "," + CryptoUtils.encrypt(password));
    }

    AuthenticationManager authManager = new AuthenticationManager();

    assertTrue(authManager.validateCredentials(username, password));
  }

  @Test
  @DisplayName("Should reject incorrect password")
  void testValidateIncorrectPassword() throws IOException {
    String username = "user";
    String password = "correctpass";

    // Write encrypted credentials
    try (FileWriter writer = new FileWriter(loginFile)) {
      writer.write(CryptoUtils.encrypt(username) + "," + CryptoUtils.encrypt(password));
    }

    AuthenticationManager authManager = new AuthenticationManager();

    assertFalse(authManager.validateCredentials(username, "wrongpass"));
  }

  @Test
  @DisplayName("Should reject non-existent username")
  void testValidateNonExistentUser() throws IOException {
    String username = "user";
    String password = "pass";

    // Write encrypted credentials
    try (FileWriter writer = new FileWriter(loginFile)) {
      writer.write(CryptoUtils.encrypt(username) + "," + CryptoUtils.encrypt(password));
    }

    AuthenticationManager authManager = new AuthenticationManager();

    assertFalse(authManager.validateCredentials("nonexistent", "pass"));
  }

  @Test
  @DisplayName("Should check if user exists")
  void testUserExists() throws IOException {
    String username = "existinguser";
    String password = "password";

    // Write encrypted credentials
    try (FileWriter writer = new FileWriter(loginFile)) {
      writer.write(CryptoUtils.encrypt(username) + "," + CryptoUtils.encrypt(password));
    }

    AuthenticationManager authManager = new AuthenticationManager();

    assertTrue(authManager.userExists(username));
    assertFalse(authManager.userExists("nonexistent"));
  }

  @Test
  @DisplayName("Should return copy of login info map")
  void testGetLoginInfoReturnsCopy() throws IOException {
    String username = "user";
    String password = "pass";

    try (FileWriter writer = new FileWriter(loginFile)) {
      writer.write(CryptoUtils.encrypt(username) + "," + CryptoUtils.encrypt(password));
    }

    AuthenticationManager authManager = new AuthenticationManager();
    HashMap<String, String> loginInfo1 = authManager.getLoginInfo();
    HashMap<String, String> loginInfo2 = authManager.getLoginInfo();

    // Modifying one should not affect the other
    loginInfo1.put("newuser", "newpass");

    assertNotEquals(loginInfo1.size(), loginInfo2.size());
  }

  @Test
  @DisplayName("Should handle malformed CSV gracefully")
  void testHandleMalformedCSV() throws IOException {
    // Write invalid CSV (only one field)
    try (FileWriter writer = new FileWriter(loginFile)) {
      writer.write("onlyonefield");
    }

    // Should not throw exception
    assertDoesNotThrow(() -> new AuthenticationManager());
  }

  @Test
  @DisplayName("Should handle empty lines in file")
  void testHandleEmptyLines() throws IOException {
    try (FileWriter writer = new FileWriter(loginFile)) {
      writer.write("   \n");
    }

    AuthenticationManager authManager = new AuthenticationManager();
    HashMap<String, String> loginInfo = authManager.getLoginInfo();

    assertTrue(loginInfo.isEmpty());
  }
}
