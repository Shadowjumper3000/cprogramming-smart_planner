package com.smartplanner.ui.pages;

import com.smartplanner.model.EmailConfig;
import com.smartplanner.service.EmailService;
import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Page for configuring email settings for reminders.
 */
public class EmailSettingsPage extends BasePage implements ActionListener {

    private JLabel titleLabel;
    private JLabel smtpHostLabel;
    private JLabel smtpPortLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel fromEmailLabel;
    private JLabel toEmailLabel;
    private JLabel useTLSLabel;

    private JTextField smtpHostField;
    private JTextField smtpPortField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fromEmailField;
    private JTextField toEmailField;
    private JCheckBox useTLSCheckbox;

    private JButton saveButton;
    private JButton testButton;
    private JButton backButton;

    private EmailConfig emailConfig;
    private EmailService emailService;

    public EmailSettingsPage(String userID) {
        this.userID = userID;
        this.emailConfig = EmailConfig.loadConfig();
        this.emailService = new EmailService(emailConfig);
        initializeComponents();
        setupLayout();
        loadCurrentSettings();
        setupFrame();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Email Settings");
        
        titleLabel = new JLabel("Email Reminder Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        smtpHostLabel = new JLabel("SMTP Host:");
        smtpPortLabel = new JLabel("SMTP Port:");
        usernameLabel = new JLabel("Email Username:");
        passwordLabel = new JLabel("Email Password:");
        fromEmailLabel = new JLabel("From Email:");
        toEmailLabel = new JLabel("To Email:");
        useTLSLabel = new JLabel("Use TLS:");

        smtpHostField = new JTextField();
        smtpPortField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        fromEmailField = new JTextField();
        toEmailField = new JTextField();
        useTLSCheckbox = new JCheckBox();

        saveButton = new JButton("Save Settings");
        testButton = new JButton("Send Test Email");
        backButton = new JButton("Back");

        saveButton.addActionListener(this);
        testButton.addActionListener(this);
        backButton.addActionListener(this);
    }

    private void setupLayout() {
        frame.setLayout(null);

        // Title
        titleLabel.setBounds(50, 20, 400, 30);
        frame.add(titleLabel);

        // Instructions panel
        JTextArea instructions = new JTextArea(
            "SIMPLE SETUP:\n\n" +
            "1. Your Email: Enter the email you want to send reminders FROM\n" +
            "2. Send To: Enter where you want reminders sent (can be same email)\n" +
            "3. Password: Your regular email password\n\n" +
            "Default settings work for most email providers!"
        );
        instructions.setBounds(50, 60, 480, 120);
        instructions.setEditable(false);
        instructions.setBackground(new Color(255, 255, 200));
        instructions.setFont(new Font("Arial", Font.PLAIN, 11));
        instructions.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        frame.add(instructions);

        // Your Email (simplified - combines username and from)
        JLabel yourEmailLabel = new JLabel("Your Email:");
        yourEmailLabel.setBounds(50, 195, 150, 25);
        yourEmailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        frame.add(yourEmailLabel);
        
        fromEmailField.setBounds(200, 195, 330, 25);
        frame.add(fromEmailField);

        // Send To Email
        JLabel sendToLabel = new JLabel("Send Reminders To:");
        sendToLabel.setBounds(50, 230, 150, 25);
        sendToLabel.setFont(new Font("Arial", Font.BOLD, 12));
        frame.add(sendToLabel);
        
        toEmailField.setBounds(200, 230, 330, 25);
        frame.add(toEmailField);

        // Password
        JLabel passLabel = new JLabel("Email Password:");
        passLabel.setBounds(50, 265, 150, 25);
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        frame.add(passLabel);
        
        passwordField.setBounds(200, 265, 330, 25);
        frame.add(passwordField);

        // Advanced settings toggle
        JButton advancedButton = new JButton("▼ Advanced Settings");
        advancedButton.setBounds(50, 305, 180, 30);
        advancedButton.setFocusable(false);
        
        JPanel advancedPanel = new JPanel(null);
        advancedPanel.setBounds(50, 345, 480, 130);
        advancedPanel.setBorder(BorderFactory.createTitledBorder("Advanced"));
        advancedPanel.setVisible(false);

        // SMTP Host (in advanced)
        smtpHostLabel.setBounds(10, 25, 100, 25);
        smtpHostField.setBounds(120, 25, 200, 25);
        advancedPanel.add(smtpHostLabel);
        advancedPanel.add(smtpHostField);

        // SMTP Port (in advanced)
        smtpPortLabel.setBounds(10, 60, 100, 25);
        smtpPortField.setBounds(120, 60, 80, 25);
        advancedPanel.add(smtpPortLabel);
        advancedPanel.add(smtpPortField);

        // Username (in advanced, hidden by default)
        usernameLabel.setBounds(10, 95, 100, 25);
        usernameField.setBounds(120, 95, 200, 25);
        advancedPanel.add(usernameLabel);
        advancedPanel.add(usernameField);

        // Use TLS (in advanced)
        useTLSLabel.setBounds(240, 60, 80, 25);
        useTLSCheckbox.setBounds(320, 60, 25, 25);
        advancedPanel.add(useTLSLabel);
        advancedPanel.add(useTLSCheckbox);

        advancedButton.addActionListener(e -> {
            advancedPanel.setVisible(!advancedPanel.isVisible());
            advancedButton.setText(advancedPanel.isVisible() ? "▲ Advanced Settings" : "▼ Advanced Settings");
            frame.setSize(580, advancedPanel.isVisible() ? 580 : 480);
        });

        frame.add(advancedButton);
        frame.add(advancedPanel);

        // Buttons
        saveButton.setBounds(50, 345, 130, 35);
        testButton.setBounds(190, 345, 150, 35);
        backButton.setBounds(350, 345, 100, 35);
        frame.add(saveButton);
        frame.add(testButton);
        frame.add(backButton);

        frame.setSize(580, 480);
    }

    private void loadCurrentSettings() {
        smtpHostField.setText(emailConfig.getSmtpHost());
        smtpPortField.setText(String.valueOf(emailConfig.getSmtpPort()));
        usernameField.setText(emailConfig.getUsername());
        passwordField.setText(emailConfig.getPassword());
        fromEmailField.setText(emailConfig.getFromEmail());
        toEmailField.setText(emailConfig.getToEmail());
        useTLSCheckbox.setSelected(emailConfig.isUseTLS());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            saveSettings();
        } else if (e.getSource() == testButton) {
            testEmailSettings();
        } else if (e.getSource() == backButton) {
            new PlannerPage(userID);
            frame.dispose();
        }
    }

    private void saveSettings() {
        try {
            String fromEmail = fromEmailField.getText().trim();
            
            // Auto-sync username with from email if username is empty
            if (usernameField.getText().trim().isEmpty()) {
                usernameField.setText(fromEmail);
            }
            
            emailConfig.setSmtpHost(smtpHostField.getText().trim());
            emailConfig.setSmtpPort(Integer.parseInt(smtpPortField.getText().trim()));
            emailConfig.setUsername(usernameField.getText().trim());
            emailConfig.setPassword(new String(passwordField.getPassword()));
            emailConfig.setFromEmail(fromEmail);
            emailConfig.setToEmail(toEmailField.getText().trim());
            emailConfig.setUseTLS(useTLSCheckbox.isSelected());

            emailConfig.saveConfig();
            emailService.setConfig(emailConfig);

            JOptionPane.showMessageDialog(frame,
                    "Email settings saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Invalid port number. Please enter a valid number.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "Error saving settings: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void testEmailSettings() {
        // First save the current settings
        saveSettings();

        // Show a progress dialog
        JDialog progressDialog = new JDialog(frame, "Sending Test Email", true);
        JLabel progressLabel = new JLabel("Sending test email, please wait...");
        progressLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        progressDialog.add(progressLabel);
        progressDialog.pack();
        progressDialog.setLocationRelativeTo(frame);

        // Send email in a separate thread
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private String errorDetails = "";
            
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    return emailService.sendTestEmail();
                } catch (Exception e) {
                    errorDetails = e.getMessage();
                    throw e;
                }
            }

            @Override
            protected void done() {
                progressDialog.dispose();
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(frame,
                                "✓ Test email sent successfully!\n\nCheck your inbox (and spam folder).",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    // Get the root cause message
                    Throwable cause = ex.getCause();
                    String message = cause != null ? cause.getMessage() : ex.getMessage();
                    
                    // Create detailed error message
                    StringBuilder errorMsg = new StringBuilder();
                    errorMsg.append("Failed to send test email.\n\n");
                    errorMsg.append("Error: ").append(message).append("\n\n");
                    errorMsg.append("Common solutions:\n");
                    errorMsg.append("• Check your email and password are correct\n");
                    errorMsg.append("• Verify internet connection\n");
                    errorMsg.append("• Gmail with 2FA: Use an App Password\n");
                    errorMsg.append("• Try expanding Advanced Settings and checking SMTP details\n\n");
                    errorMsg.append("Check the console output for detailed logs.");
                    
                    JOptionPane.showMessageDialog(frame,
                            errorMsg.toString(),
                            "Email Test Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        progressDialog.setVisible(true);
    }
}
