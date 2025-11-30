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
        titleLabel.setBounds(50, 20, 300, 30);
        frame.add(titleLabel);

        // SMTP Host
        smtpHostLabel.setBounds(50, 70, 150, 25);
        smtpHostField.setBounds(200, 70, 250, 25);
        frame.add(smtpHostLabel);
        frame.add(smtpHostField);

        // SMTP Port
        smtpPortLabel.setBounds(50, 105, 150, 25);
        smtpPortField.setBounds(200, 105, 250, 25);
        frame.add(smtpPortLabel);
        frame.add(smtpPortField);

        // Use TLS
        useTLSLabel.setBounds(50, 140, 150, 25);
        useTLSCheckbox.setBounds(200, 140, 25, 25);
        frame.add(useTLSLabel);
        frame.add(useTLSCheckbox);

        // Username
        usernameLabel.setBounds(50, 175, 150, 25);
        usernameField.setBounds(200, 175, 250, 25);
        frame.add(usernameLabel);
        frame.add(usernameField);

        // Password
        passwordLabel.setBounds(50, 210, 150, 25);
        passwordField.setBounds(200, 210, 250, 25);
        frame.add(passwordLabel);
        frame.add(passwordField);

        // From Email
        fromEmailLabel.setBounds(50, 245, 150, 25);
        fromEmailField.setBounds(200, 245, 250, 25);
        frame.add(fromEmailLabel);
        frame.add(fromEmailField);

        // To Email
        toEmailLabel.setBounds(50, 280, 150, 25);
        toEmailField.setBounds(200, 280, 250, 25);
        frame.add(toEmailLabel);
        frame.add(toEmailField);

        // Info label
        JLabel infoLabel = new JLabel("<html>Note: For Gmail, use 'smtp.gmail.com' port 587<br/>" +
                                      "and enable 'App Passwords' in your Google account.</html>");
        infoLabel.setBounds(50, 315, 400, 40);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        frame.add(infoLabel);

        // Buttons
        saveButton.setBounds(50, 370, 130, 35);
        testButton.setBounds(190, 370, 150, 35);
        backButton.setBounds(350, 370, 100, 35);
        frame.add(saveButton);
        frame.add(testButton);
        frame.add(backButton);

        frame.setSize(500, 500);
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
            new MainPage(userID);
            frame.dispose();
        }
    }

    private void saveSettings() {
        try {
            emailConfig.setSmtpHost(smtpHostField.getText().trim());
            emailConfig.setSmtpPort(Integer.parseInt(smtpPortField.getText().trim()));
            emailConfig.setUsername(usernameField.getText().trim());
            emailConfig.setPassword(new String(passwordField.getPassword()));
            emailConfig.setFromEmail(fromEmailField.getText().trim());
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
            @Override
            protected Boolean doInBackground() throws Exception {
                return emailService.sendTestEmail();
            }

            @Override
            protected void done() {
                progressDialog.dispose();
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(frame,
                                "Test email sent successfully!\nCheck your inbox.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Failed to send test email.\nPlease check your settings and try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Error sending test email: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        progressDialog.setVisible(true);
    }
}
