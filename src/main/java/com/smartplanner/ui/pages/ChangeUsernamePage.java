package com.smartplanner.ui.pages;

import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Page for changing username.
 */
public class ChangeUsernamePage extends BasePage implements ActionListener {

    private String oldUserName;

    public ChangeUsernamePage(String oldUserName, HashMap<String, String> loginInfoOriginal) {
        this.oldUserName = oldUserName;
        this.userID = oldUserName;
        this.loginInfo = loginInfoOriginal;
        initializeComponents();
        setupLayout();
        setupFrame();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Change Username");
        button1 = new JButton("Change");
        button2 = new JButton("Reset");
        undoBackButton = new JButton("Cancel");
        label1 = new JLabel("New user name");
        field1 = new JTextField();
        passwordField1 = new JPasswordField();
        label2 = new JLabel("Password");
        messageLabel = new JLabel();
    }

    private void setupLayout() {
        label1.setBounds(25, 75, 200, 25);
        field1.setBounds(125, 75, 200, 25);

        label2.setBounds(25, 150, 200, 25);
        passwordField1.setBounds(125, 150, 200, 25);

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
        frame.add(field1);
        frame.add(label2);
        frame.add(passwordField1);
        frame.add(button1);
        frame.add(button2);
        frame.add(undoBackButton);
        frame.add(messageLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button2) {
            // Reset fields
            field1.setText("");
            passwordField1.setText("");
        } else if (e.getSource() == undoBackButton) {
            // Cancel and return to main page
            frame.dispose();
            new MainPage(this.oldUserName);
        } else if (e.getSource() == button1) {
            // Handle username change
            handleUsernameChange();
        }
    }

    private void handleUsernameChange() {
        String passwordInput = String.valueOf(passwordField1.getPassword());
        String newID = field1.getText();

        if (newID.length() < 5) {
            messageLabel.setForeground(java.awt.Color.red);
            messageLabel.setText("User name too short (5 characters minimum)");
        } else if (loginInfo.containsKey(newID)) {
            messageLabel.setForeground(java.awt.Color.red);
            messageLabel.setText("Username already exists");
        } else if (loginInfo.get(oldUserName).equals(passwordInput)) {
            // TODO: Implement username change functionality
            JOptionPane.showMessageDialog(frame, "Username change functionality coming soon");
            frame.dispose();
            new MainPage(newID);
        } else {
            messageLabel.setForeground(java.awt.Color.red);
            messageLabel.setText("Incorrect password");
        }
    }
}