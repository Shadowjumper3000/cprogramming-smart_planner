package com.smartplanner.ui.pages;

import com.smartplanner.ui.components.BasePage;
import com.smartplanner.utils.CryptoUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;

/**
 * Sign up page for new user registration.
 */
public class SignUpPage extends BasePage implements ActionListener {

    private JLabel newUserIDLabel;

    public SignUpPage(HashMap<String, String> loginInfoOriginal) {
        this.loginInfo = loginInfoOriginal;
        initializeComponents();
        setupLayout();
        setupFrame();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Sign Up");
        button1 = new JButton("Sign Up");
        button2 = new JButton("Reset");
        undoBackButton = new JButton("Cancel");
        field1 = new JTextField();
        passwordField1 = new JPasswordField();
        passwordField2 = new JPasswordField();
        label1 = new JLabel("Password");
        label2 = new JLabel("Password 2");
        newUserIDLabel = new JLabel("New user ID");
        messageLabel = new JLabel();
    }

    private void setupLayout() {
        // Labels and fields
        newUserIDLabel.setBounds(50, 100, 75, 25);
        field1.setBounds(125, 100, 200, 25);

        label1.setBounds(50, 150, 75, 25);
        passwordField1.setBounds(125, 150, 200, 25);

        label2.setBounds(50, 200, 75, 25);
        passwordField2.setBounds(125, 200, 200, 25);

        // Buttons
        button1.setBounds(125, 250, 100, 25);
        button1.setFocusable(false);
        button1.addActionListener(this);

        button2.setBounds(225, 250, 100, 25);
        button2.setFocusable(false);
        button2.addActionListener(this);

        undoBackButton.setBounds(175, 300, 100, 25);
        undoBackButton.setFocusable(false);
        undoBackButton.addActionListener(this);

        messageLabel.setBounds(125, 350, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 12));

        // Add components to frame
        frame.add(newUserIDLabel);
        frame.add(field1);
        frame.add(label1);
        frame.add(passwordField1);
        frame.add(label2);
        frame.add(passwordField2);
        frame.add(button1);
        frame.add(button2);
        frame.add(undoBackButton);
        frame.add(messageLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button2) {
            // Reset button - clear fields
            clearFields();
        } else if (e.getSource() == undoBackButton) {
            // Cancel button - return to landing page
            frame.dispose();
            new LandingPage();
        } else if (e.getSource() == button1) {
            // Sign up button - handle registration
            handleSignUp();
        }
    }

    private void clearFields() {
        field1.setText("");
        passwordField1.setText("");
        passwordField2.setText("");
    }

    private void handleSignUp() {
        String userId = field1.getText();
        String password1 = String.valueOf(passwordField1.getPassword());
        String password2 = String.valueOf(passwordField2.getPassword());

        if (userId.length() < 5 || password1.length() < 5) {
            JOptionPane.showMessageDialog(null,
                    "Password or username is too short (5 characters minimum)");
        } else if (!password1.equals(password2)) {
            messageLabel.setForeground(Color.red);
            messageLabel.setText("Passwords do not match");
        } else if (loginInfo.containsKey(userId)) {
            JOptionPane.showMessageDialog(null, "Username already exists");
        } else {
            saveUserCredentials(userId, password1);
        }
    }

    private void saveUserCredentials(String userId, String password) {
        String filePath = "data" + File.separator + "login_info.csv";

        try (FileWriter fw = new FileWriter(filePath, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw)) {

            // Write encrypted data to login file
            pw.println(CryptoUtils.encrypt(userId) + "," + CryptoUtils.encrypt(password));

            JOptionPane.showMessageDialog(null, "Information saved");

            // Notify about successful sign up
            messageLabel.setForeground(Color.green);
            messageLabel.setText("Sign up successful");

            // Redirect to main page
            frame.dispose();
            new MainPage(userId);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Information failed to save");
            System.err.println("Error saving user credentials: " + ex.getMessage());
        }
    }
}