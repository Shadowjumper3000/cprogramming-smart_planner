package com.smartplanner.ui.pages;

import com.smartplanner.auth.AuthenticationManager;
import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Main application page with navigation to planner and time table features.
 */
public class MainPage extends BasePage implements ActionListener {

    private JMenuBar menuBar;
    private JMenu profileMenu;
    private JMenuItem changeProfileName;
    private JMenuItem changeProfilePassword;

    public MainPage(String userID) {
        this.userID = userID;
        initializeComponents();
        setupLayout();
        setupMenu();
        setupFrame();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Main");
        button1 = new JButton("Planner");
        button2 = new JButton("Time Table");
        
        menuBar = new JMenuBar();
        profileMenu = new JMenu(userID);
        changeProfileName = new JMenuItem("Change profile name");
        changeProfilePassword = new JMenuItem("Change profile password");
    }

    private void setupLayout() {
        // Button1 (Planner) settings
        button1.setBounds(100, 200, 200, 40);
        button1.setFocusable(false);
        button1.addActionListener(this);
        frame.add(button1);

        // Button2 (Time Table) settings
        button2.setBounds(100, 100, 200, 40);
        button2.setFocusable(false);
        button2.addActionListener(this);
        frame.add(button2);
    }

    private void setupMenu() {
        // Add action listeners to menu items
        changeProfileName.addActionListener(this);
        changeProfilePassword.addActionListener(this);

        // Build menu structure
        profileMenu.add(changeProfileName);
        profileMenu.add(changeProfilePassword);
        menuBar.add(profileMenu);

        // Set menu bar and keyboard shortcuts
        frame.setJMenuBar(menuBar);
        changeProfilePassword.setMnemonic(KeyEvent.VK_P);
        changeProfileName.setMnemonic(KeyEvent.VK_N);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeProfileName) {
            AuthenticationManager authManager = new AuthenticationManager();
            new ChangeUsernamePage(this.userID, authManager.getLoginInfo());
            frame.dispose();
        } else if (e.getSource() == changeProfilePassword) {
            AuthenticationManager authManager = new AuthenticationManager();
            new ChangePasswordPage(authManager.getLoginInfo(), this.userID);
            frame.dispose();
        } else if (e.getSource() == button1) {
            new PlannerPage(this.userID);
            frame.dispose();
        } else if (e.getSource() == button2) {
            new TimeTablePage(this.userID);
            frame.dispose();
        }
    }
}