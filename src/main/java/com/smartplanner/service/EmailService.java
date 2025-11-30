package com.smartplanner.service;

import com.smartplanner.model.EmailConfig;
import com.smartplanner.model.Task;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Service for sending email reminders.
 */
public class EmailService {
  private EmailConfig config;

  public EmailService() {
    this.config = EmailConfig.loadConfig();
  }

  public EmailService(EmailConfig config) {
    this.config = config;
  }

  /**
   * Reloads the email configuration from file.
   */
  public void reloadConfig() {
    this.config = EmailConfig.loadConfig();
  }

  /**
   * Sends an email reminder for a task.
   */
  public boolean sendTaskReminder(Task task) {
    if (!config.isValid()) {
      System.err.println("Email configuration is not valid. Please configure email settings.");
      return false;
    }

    String subject = "SmartPlanner Reminder: " + task.getTitle();
    String body = buildReminderEmailBody(task);

    return sendEmail(subject, body);
  }

  /**
   * Builds the email body for a task reminder.
   */
  private String buildReminderEmailBody(Task task) {
    StringBuilder body = new StringBuilder();
    body.append("Task Reminder from SmartPlanner\n\n");
    body.append("=================================\n\n");
    body.append("Title: ").append(task.getTitle()).append("\n");
    
    if (task.getDescription() != null && !task.getDescription().isEmpty()) {
      body.append("Description: ").append(task.getDescription()).append("\n");
    }
    
    if (task.getDueDate() != null) {
      body.append("Due Date: ").append(task.getDueDate()).append("\n");
    }
    
    if (task.getDueTime() != null) {
      body.append("Due Time: ").append(task.getDueTime()).append("\n");
    }
    
    body.append("Priority: ").append(task.getPriority()).append("\n");
    
    if (task.getCategory() != null && !task.getCategory().isEmpty()) {
      body.append("Category: ").append(task.getCategory()).append("\n");
    }
    
    body.append("\n=================================\n\n");
    body.append("This is an automated reminder from SmartPlanner.\n");
    body.append("Please complete your task on time!\n");

    return body.toString();
  }

  /**
   * Sends an email with the given subject and body.
   */
  public boolean sendEmail(String subject, String body) {
    if (!config.isValid()) {
      System.err.println("Email configuration is not valid.");
      throw new RuntimeException("Email configuration incomplete. Please fill in all required fields.");
    }

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", String.valueOf(config.isUseTLS()));
    props.put("mail.smtp.host", config.getSmtpHost());
    props.put("mail.smtp.port", String.valueOf(config.getSmtpPort()));
    props.put("mail.smtp.ssl.trust", config.getSmtpHost());
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");
    props.put("mail.smtp.connectiontimeout", "10000");
    props.put("mail.smtp.timeout", "10000");
    props.put("mail.smtp.writetimeout", "10000");
    
    // Enable debug mode for troubleshooting
    props.put("mail.debug", "true");

    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(config.getUsername(), config.getPassword());
      }
    });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(config.getFromEmail()));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(config.getToEmail()));
      message.setSubject(subject);
      message.setText(body);

      System.out.println("Attempting to send email...");
      System.out.println("From: " + config.getFromEmail());
      System.out.println("To: " + config.getToEmail());
      System.out.println("SMTP: " + config.getSmtpHost() + ":" + config.getSmtpPort());
      
      Transport.send(message);
      System.out.println("Email sent successfully!");
      return true;

    } catch (MessagingException e) {
      System.err.println("Failed to send email: " + e.getMessage());
      e.printStackTrace();
      
      // Provide more specific error messages
      String errorMsg = e.getMessage();
      if (errorMsg != null) {
        if (errorMsg.contains("authentication failed") || errorMsg.contains("535")) {
          throw new RuntimeException("Authentication failed. Check your email and password.\n" +
                                   "Gmail users: You may need an App Password if 2-Step Verification is enabled.", e);
        } else if (errorMsg.contains("Could not connect") || errorMsg.contains("timeout")) {
          throw new RuntimeException("Cannot connect to email server. Check your internet connection.", e);
        } else if (errorMsg.contains("Unknown SMTP host")) {
          throw new RuntimeException("Invalid SMTP host: " + config.getSmtpHost(), e);
        }
      }
      throw new RuntimeException("Email send failed: " + errorMsg, e);
    }
  }

  /**
   * Tests the email configuration by sending a test email.
   */
  public boolean sendTestEmail() {
    String subject = "SmartPlanner Email Test";
    String body = "This is a test email from SmartPlanner.\n\n" +
                  "If you received this, your email configuration is working correctly!";
    return sendEmail(subject, body);
  }

  public EmailConfig getConfig() {
    return config;
  }

  public void setConfig(EmailConfig config) {
    this.config = config;
  }
}
