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
        frame.setLayout(new BorderLayout(10, 10));

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Title
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        // Instructions panel
        JTextArea instructions = new JTextArea(
            "SIMPLE SETUP:\n\n" +
            "1. Your Email: Enter the email you want to send reminders FROM\n" +
            "2. Send To: Enter where you want reminders sent (can be same email)\n" +
            "3. Password: Your regular email password\n\n" +
            "Default settings work for most email providers!"
        );
        instructions.setEditable(false);
        instructions.setBackground(new Color(255, 255, 200));
        instructions.setFont(new Font("Arial", Font.PLAIN, 11));
        instructions.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        instructions.setMaximumSize(new Dimension(550, 120));
        instructions.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(instructions);
        contentPanel.add(Box.createVerticalStrut(20));

        // Simple form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(550, 150));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Your Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel yourEmailLabel = new JLabel("Your Email:");
        yourEmailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(yourEmailLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        fromEmailField.setPreferredSize(new Dimension(300, 25));
        formPanel.add(fromEmailField, gbc);

        // Send To
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel sendToLabel = new JLabel("Send Reminders To:");
        sendToLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(sendToLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        toEmailField.setPreferredSize(new Dimension(300, 25));
        formPanel.add(toEmailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel passLabel = new JLabel("Email Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        passwordField.setPreferredSize(new Dimension(300, 25));
        formPanel.add(passwordField, gbc);

        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Advanced settings toggle
        JButton advancedButton = new JButton("▼ Advanced Settings");
        advancedButton.setFocusable(false);
        advancedButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(advancedButton);
        contentPanel.add(Box.createVerticalStrut(10));

        // Advanced panel
        JPanel advancedPanel = new JPanel(new GridBagLayout());
        advancedPanel.setBorder(BorderFactory.createTitledBorder("Advanced"));
        advancedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        advancedPanel.setMaximumSize(new Dimension(550, 150));
        advancedPanel.setVisible(false);

        GridBagConstraints advGbc = new GridBagConstraints();
        advGbc.insets = new Insets(5, 5, 5, 5);
        advGbc.anchor = GridBagConstraints.WEST;
        advGbc.fill = GridBagConstraints.HORIZONTAL;

        // SMTP Host
        advGbc.gridx = 0; advGbc.gridy = 0; advGbc.weightx = 0;
        advancedPanel.add(smtpHostLabel, advGbc);
        advGbc.gridx = 1; advGbc.weightx = 1;
        smtpHostField.setPreferredSize(new Dimension(200, 25));
        advancedPanel.add(smtpHostField, advGbc);

        // SMTP Port
        advGbc.gridx = 0; advGbc.gridy = 1; advGbc.weightx = 0;
        advancedPanel.add(smtpPortLabel, advGbc);
        advGbc.gridx = 1; advGbc.weightx = 0;
        smtpPortField.setPreferredSize(new Dimension(80, 25));
        advancedPanel.add(smtpPortField, advGbc);

        // Use TLS
        advGbc.gridx = 2; advGbc.weightx = 0;
        advancedPanel.add(useTLSLabel, advGbc);
        advGbc.gridx = 3;
        advancedPanel.add(useTLSCheckbox, advGbc);

        // Username
        advGbc.gridx = 0; advGbc.gridy = 2; advGbc.weightx = 0;
        advancedPanel.add(usernameLabel, advGbc);
        advGbc.gridx = 1; advGbc.weightx = 1; advGbc.gridwidth = 3;
        usernameField.setPreferredSize(new Dimension(200, 25));
        advancedPanel.add(usernameField, advGbc);

        advancedButton.addActionListener(e -> {
            boolean isVisible = !advancedPanel.isVisible();
            advancedPanel.setVisible(isVisible);
            advancedButton.setText(isVisible ? "▲ Advanced Settings" : "▼ Advanced Settings");
            frame.pack();
            frame.setSize(600, isVisible ? 600 : 480);
        });

        contentPanel.add(advancedPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(saveButton);
        buttonPanel.add(testButton);
        buttonPanel.add(backButton);
        
        contentPanel.add(buttonPanel);

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setSize(600, 480);
    }
    
    @Override
    protected void setupFrame() {
        if (frame != null) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null); // Center on screen
        }
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
            
            // ALWAYS sync username with from email (most email servers use full email as username)
            usernameField.setText(fromEmail);
            
            emailConfig.setSmtpHost(smtpHostField.getText().trim());
            emailConfig.setSmtpPort(Integer.parseInt(smtpPortField.getText().trim()));
            emailConfig.setUsername(fromEmail); // Use full email address as username
            emailConfig.setPassword(new String(passwordField.getPassword()));
            emailConfig.setFromEmail(fromEmail);
            emailConfig.setToEmail(toEmailField.getText().trim());
            emailConfig.setUseTLS(useTLSCheckbox.isSelected());

            emailConfig.saveConfig();
            emailService.setConfig(emailConfig);

            // Show Gmail-specific warning if needed
            if (fromEmail.toLowerCase().contains("@gmail.com")) {
                JOptionPane.showMessageDialog(frame,
                        "Settings saved!\n\n" +
                        "⚠️ GMAIL USERS: If test fails, you need an App Password:\n" +
                        "1. Go to myaccount.google.com/security\n" +
                        "2. Enable 2-Step Verification\n" +
                        "3. Create an App Password for 'Mail'\n" +
                        "4. Use that 16-character password here instead",
                        "Gmail Setup Required",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Email settings saved successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }

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
                    
                    // Check if it's a Gmail authentication issue
                    boolean isGmail = fromEmailField.getText().toLowerCase().contains("@gmail.com");
                    boolean isAuthError = message != null && (message.contains("535") || message.contains("authentication"));
                    
                    // Create detailed error message
                    StringBuilder errorMsg = new StringBuilder();
                    errorMsg.append("❌ Failed to send test email.\n\n");
                    
                    if (isGmail && isAuthError) {
                        errorMsg.append("⚠️ GMAIL REQUIRES APP PASSWORD:\n\n");
                        errorMsg.append("Gmail blocks regular passwords for security.\n");
                        errorMsg.append("You MUST use an App Password:\n\n");
                        errorMsg.append("1. Visit: myaccount.google.com/security\n");
                        errorMsg.append("2. Enable '2-Step Verification'\n");
                        errorMsg.append("3. Go to '2-Step Verification' → 'App passwords'\n");
                        errorMsg.append("4. Select 'Mail' and 'Windows Computer'\n");
                        errorMsg.append("5. Copy the 16-character password\n");
                        errorMsg.append("6. Paste it in the 'Email Password' field\n\n");
                        errorMsg.append("Your regular Gmail password will NOT work!");
                    } else {
                        errorMsg.append("Error: ").append(message).append("\n\n");
                        errorMsg.append("Common solutions:\n");
                        errorMsg.append("• Check your email and password are correct\n");
                        errorMsg.append("• Verify internet connection\n");
                        if (isGmail) {
                            errorMsg.append("• Gmail: You MUST use an App Password\n");
                        }
                        errorMsg.append("• Check Advanced Settings (SMTP, port, etc.)\n");
                    }
                    
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
