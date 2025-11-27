package com.smartplanner.core;

import com.smartplanner.data.FileManager;
import com.smartplanner.ui.pages.LandingPage;

/**
 * Main application entry point for Smart Planner.
 * Initializes required data files and launches the application.
 */
public class SmartPlannerApplication {

    public static void main(String[] args) {
        // Initialize application data files
        FileManager fileManager = new FileManager();
        fileManager.initializeDataFiles();
        
        // Launch the application
        new LandingPage();
    }
}