package com.smartplanner.ui.components;

import com.smartplanner.utils.CryptoUtils;

import javax.swing.*;
import java.util.HashMap;

/**
 * Abstract base class for all UI pages in the application.
 * Provides common UI components and functionality.
 */
public abstract class BasePage {
    protected JFrame frame;
    protected JButton button1;
    protected JButton button2;
    protected JButton undoBackButton;
    protected JTextField field1;
    protected JTextField field2;
    protected JPasswordField passwordField1;
    protected JPasswordField passwordField2;
    protected JLabel label1;
    protected JLabel label2;
    protected JLabel messageLabel;
    protected String userID;
    protected HashMap<String, String> loginInfo;

    /**
     * Encrypts a string using the default encryption method.
     * 
     * @param original the string to encrypt
     * @return the encrypted string
     */
    protected String encrypt(String original) {
        return CryptoUtils.encrypt(original);
    }

    /**
     * Decrypts a string using the default decryption method.
     * 
     * @param encrypted the string to decrypt
     * @return the decrypted string
     */
    protected String decrypt(String encrypted) {
        return CryptoUtils.decrypt(encrypted);
    }
    
    /**
     * Sets up common frame properties.
     */
    protected void setupFrame() {
        if (frame != null) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(420, 420);
            frame.setLayout(null);
            frame.setVisible(true);
            frame.setResizable(false);
        }
    }
}