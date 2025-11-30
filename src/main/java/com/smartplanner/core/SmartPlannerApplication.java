package com.smartplanner.core;

import com.smartplanner.data.FileManager;
import com.smartplanner.service.EmailService;
import com.smartplanner.service.PlannerService;
import com.smartplanner.service.ReminderService;
import com.smartplanner.ui.pages.LandingPage;

/**
 * Main application entry point for Smart Planner.
 * Initializes required data files and launches the application.
 */
public class SmartPlannerApplication {

    private static ReminderService reminderService;

    public static void main(String[] args) {
        // Initialize application data files
        FileManager fileManager = new FileManager();
        fileManager.initializeDataFiles();
        
        // Initialize reminder service
        PlannerService plannerService = new PlannerService();
        EmailService emailService = new EmailService();
        reminderService = new ReminderService(plannerService, emailService);
        
        // Start the reminder service
        reminderService.start();
        
        // Add shutdown hook to stop reminder service gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (reminderService != null) {
                reminderService.stop();
            }
        }));
        
        // Launch the application
        new LandingPage();
    }

    public static ReminderService getReminderService() {
        return reminderService;
    }
}