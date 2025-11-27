package com.smartplanner.auth;

import com.smartplanner.utils.CryptoUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Handles user authentication and credential management.
 */
public class AuthenticationManager {

    private static final String LOGIN_INFO_FILE = "data" + File.separator + "login_info.csv";
    private final HashMap<String, String> loginInfo = new HashMap<>();

    /**
     * Creates a new AuthenticationManager and loads login information.
     */
    public AuthenticationManager() {
        loadLoginInformation();
    }

    /**
     * Loads login information from the persistent file.
     */
    private void loadLoginInformation() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOGIN_INFO_FILE))) {
            String line = reader.readLine();

            if (line != null && !line.trim().isEmpty()) {
                String[] row = line.split(",");

                // Debug output
                System.out.println(Arrays.toString(row) + " -row");
                System.out.println(row.length + " -row.length()");

                // Store the decrypted username and password
                if (row.length >= 2) {
                    String decryptedUsername = CryptoUtils.decrypt(row[0]);
                    String decryptedPassword = CryptoUtils.decrypt(row[1]);
                    loginInfo.put(decryptedUsername, decryptedPassword);
                }

                // Debug output
                System.out.println(loginInfo + " -logininfo");
            }
        } catch (Exception e) {
            System.err.println("Error loading login information: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns the login information map.
     * 
     * @return HashMap containing username-password pairs
     */
    public HashMap<String, String> getLoginInfo() {
        return new HashMap<>(loginInfo);
    }

    /**
     * Validates user credentials.
     * 
     * @param username the username to validate
     * @param password the password to validate
     * @return true if credentials are valid, false otherwise
     */
    public boolean validateCredentials(String username, String password) {
        return loginInfo.containsKey(username) && loginInfo.get(username).equals(password);
    }

    /**
     * Checks if a username already exists.
     * 
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    public boolean userExists(String username) {
        return loginInfo.containsKey(username);
    }
}