package com.smartplanner.ui.pages;

import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Page for changing user password.
 */
public class ChangePasswordPage extends BasePage implements ActionListener {

    public ChangePasswordPage(HashMap<String, String> loginInfoOriginal, String userID) {
        this.loginInfo = loginInfoOriginal;
        this.userID = userID;
        initializeComponents();
        setupLayout();
        setupFrame();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Change Password");
        button1 = new JButton("Change");
        button2 = new JButton("Reset");
        undoBackButton = new JButton("Cancel");
        passwordField1 = new JPasswordField();
        passwordField2 = new JPasswordField();
        label1 = new JLabel("New Password 1");
        label2 = new JLabel("New Password 2");
        messageLabel = new JLabel();
    }

    private void setupLayout() {
        label1.setBounds(50, 100, 120, 25);
        passwordField1.setBounds(175, 100, 200, 25);

        label2.setBounds(50, 150, 120, 25);
        passwordField2.setBounds(175, 150, 200, 25);

        button1.setBounds(125, 250, 100, 25);
        button1.setFocusable(false);
        button1.addActionListener(this);

        button2.setBounds(225, 250, 100, 25);
        button2.setFocusable(false);
        button2.addActionListener(this);

        undoBackButton.setBounds(175, 350, 100, 25);
        undoBackButton.setFocusable(false);
        undoBackButton.addActionListener(this);

        messageLabel.setBounds(50, 280, 250, 35);
        messageLabel.setFont(new java.awt.Font(null, java.awt.Font.ITALIC, 12));

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
            // Reset fields
            passwordField1.setText("");
            passwordField2.setText("");
        } else if (e.getSource() == undoBackButton) {
            // Cancel and return to main page
            frame.dispose();
            new MainPage(this.userID);
        } else if (e.getSource() == button1) {
            // Handle password change
            handlePasswordChange();
        }
    }

    private void handlePasswordChange() {
        String password1 = String.valueOf(passwordField1.getPassword());
        String password2 = String.valueOf(passwordField2.getPassword());

        if (!password1.equals(password2)) {
            messageLabel.setForeground(java.awt.Color.red);
            messageLabel.setText("Passwords do not match");
        } else if (password1.length() < 5) {
            messageLabel.setForeground(java.awt.Color.red);
            messageLabel.setText("Password too short (5 characters minimum)");
        } else {
            // TODO: Implement password change functionality
            JOptionPane.showMessageDialog(frame, "Password change functionality coming soon");
            frame.dispose();
            new MainPage(this.userID);
        }
    }
}