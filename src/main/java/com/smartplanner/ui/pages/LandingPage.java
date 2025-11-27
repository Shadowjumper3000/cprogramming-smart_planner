package com.smartplanner.ui.pages;

import com.smartplanner.auth.AuthenticationManager;
import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Landing page that provides options to sign up or log in.
 */
public class LandingPage extends BasePage implements ActionListener {

    public LandingPage() {
        initializeComponents();
        setupLayout();
        configureButtons();
        setupFrame();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Welcome");
        button1 = new JButton("Sign up");
        button2 = new JButton("Log in");
    }

    private void setupLayout() {
        // Button1 (Sign up) settings
        button1.setBounds(100, 200, 200, 40);
        button1.setFocusable(false);
        button1.addActionListener(this);
        frame.add(button1);

        // Button2 (Log in) settings
        button2.setBounds(100, 100, 200, 40);
        button2.setFocusable(false);
        button2.addActionListener(this);
        frame.add(button2);
    }

    private void configureButtons() {
        // Enable or disable buttons based on whether login_info.csv contains data
        String loginFile = "data" + File.separator + "login_info.csv";
        try (FileReader reader = new FileReader(loginFile)) {
            int data = reader.read();
            System.out.println(data + " - data read from " + loginFile);

            if (data > 0) {
                button1.setEnabled(false); // Disable sign up if user exists
            } else {
                button2.setEnabled(false); // Disable log in if no user exists
            }
        } catch (IOException e) {
            System.err.println("Error reading login info file: " + e.getMessage());
            // If file doesn't exist or error, enable sign up only
            button2.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            frame.dispose(); // Close the landing page

            // Navigate to sign up page
            AuthenticationManager authManager = new AuthenticationManager();
            new SignUpPage(authManager.getLoginInfo());

        } else if (e.getSource() == button2) {
            frame.dispose(); // Close the landing page

            // Navigate to log in page
            AuthenticationManager authManager = new AuthenticationManager();
            new LoginPage(authManager.getLoginInfo());
        }
    }
}