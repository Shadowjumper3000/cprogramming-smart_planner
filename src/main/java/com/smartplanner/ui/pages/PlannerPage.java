package com.smartplanner.ui.pages;

import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Planner page for managing tasks and schedules.
 */
public class PlannerPage extends BasePage implements ActionListener {

    public PlannerPage(String userID) {
        this.userID = userID;
        initializeComponents();
        setupLayout();
        setupFrame();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Planner");
        undoBackButton = new JButton("Back");
        label1 = new JLabel("Planner functionality coming soon...");
    }

    private void setupLayout() {
        label1.setBounds(50, 100, 300, 25);
        
        undoBackButton.setBounds(400, 265, 100, 25);
        undoBackButton.setFocusable(false);
        undoBackButton.addActionListener(this);

        frame.add(label1);
        frame.add(undoBackButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == undoBackButton) {
            frame.dispose();
            new MainPage(userID);
        }
    }
}