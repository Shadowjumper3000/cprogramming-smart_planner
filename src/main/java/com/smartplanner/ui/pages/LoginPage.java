package com.smartplanner.ui.pages;

import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Login page for user authentication.
 */
public class LoginPage extends BasePage implements ActionListener {

    public LoginPage(HashMap<String, String> loginInfoOriginal) {
        this.loginInfo = loginInfoOriginal;
        initializeComponents();
        setupLayout();
        setupFrame();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Login");
        button1 = new JButton("Login");
        button2 = new JButton("Reset");
        undoBackButton = new JButton("Cancel");
        field1 = new JTextField();
        passwordField1 = new JPasswordField();
        label1 = new JLabel("UserID");
        label2 = new JLabel("Password");
        messageLabel = new JLabel();
    }

    private void setupLayout() {
        // Labels
        label1.setBounds(50, 100, 75, 25);
        label2.setBounds(50, 150, 75, 25);

        // Message label
        messageLabel.setBounds(125, 300, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 25));

        // Input fields
        field1.setBounds(125, 100, 200, 25);
        passwordField1.setBounds(125, 150, 200, 25);

        // Buttons
        button1.setBounds(125, 200, 100, 25);
        button1.setFocusable(false);
        button1.addActionListener(this);

        button2.setBounds(225, 200, 100, 25);
        button2.setFocusable(false);
        button2.addActionListener(this);

        undoBackButton.setBounds(175, 250, 100, 25);
        undoBackButton.setFocusable(false);
        undoBackButton.addActionListener(this);

        // Add components to frame
        frame.add(label1);
        frame.add(label2);
        frame.add(messageLabel);
        frame.add(field1);
        frame.add(passwordField1);
        frame.add(button1);
        frame.add(button2);
        frame.add(undoBackButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button2) {
            // Reset button - clear fields
            field1.setText("");
            passwordField1.setText("");
        } else if (e.getSource() == undoBackButton) {
            // Cancel button - return to landing page
            frame.dispose();
            new LandingPage();
        } else if (e.getSource() == button1) {
            // Login button - validate credentials
            handleLogin();
        }
    }

    private void handleLogin() {
        String userID = field1.getText();
        String password = String.valueOf(passwordField1.getPassword());

        if (loginInfo.containsKey(userID)) {
            if (loginInfo.get(userID).equals(password)) {
                messageLabel.setForeground(Color.green);
                messageLabel.setText("Login successful");
                frame.dispose();
                new MainPage(userID);
            } else {
                messageLabel.setForeground(Color.red);
                messageLabel.setText("Wrong password");
            }
        } else {
            messageLabel.setForeground(Color.red);
            messageLabel.setText("Username not found");
        }
    }
}