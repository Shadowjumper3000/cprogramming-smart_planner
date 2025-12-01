package com.smartplanner.model;

import java.io.*;
import java.util.Properties;

/**
 * Configuration for email settings.
 */
public class EmailConfig {
  private static final String CONFIG_FILE = "data/email_config.properties";
  
  private String smtpHost;
  private int smtpPort;
  private String username;
  private String password;
  private String fromEmail;
  private String toEmail;
  private boolean useTLS;

  public EmailConfig() {
    // Default values for Gmail
    this.smtpHost = "smtp.gmail.com";
    this.smtpPort = 587;
    this.useTLS = true;
  }

  /**
   * Loads email configuration from file.
   */
  public static EmailConfig loadConfig() {
    EmailConfig config = new EmailConfig();
    File file = new File(CONFIG_FILE);

    if (!file.exists()) {
      return config;
    }

    try (FileInputStream fis = new FileInputStream(file)) {
      Properties props = new Properties();
      props.load(fis);

      config.setSmtpHost(props.getProperty("smtp.host", "smtp.gmail.com"));
      config.setSmtpPort(Integer.parseInt(props.getProperty("smtp.port", "587")));
      config.setUsername(props.getProperty("email.username", ""));
      config.setPassword(props.getProperty("email.password", ""));
      config.setFromEmail(props.getProperty("email.from", ""));
      config.setToEmail(props.getProperty("email.to", ""));
      config.setUseTLS(Boolean.parseBoolean(props.getProperty("smtp.tls", "true")));
    } catch (IOException | NumberFormatException e) {
      System.err.println("Error loading email config: " + e.getMessage());
    }

    return config;
  }

  /**
   * Saves email configuration to file.
   */
  public void saveConfig() {
    File file = new File(CONFIG_FILE);
    file.getParentFile().mkdirs();

    Properties props = new Properties();
    props.setProperty("smtp.host", smtpHost != null ? smtpHost : "");
    props.setProperty("smtp.port", String.valueOf(smtpPort));
    props.setProperty("email.username", username != null ? username : "");
    props.setProperty("email.password", password != null ? password : "");
    props.setProperty("email.from", fromEmail != null ? fromEmail : "");
    props.setProperty("email.to", toEmail != null ? toEmail : "");
    props.setProperty("smtp.tls", String.valueOf(useTLS));

    try (FileOutputStream fos = new FileOutputStream(file)) {
      props.store(fos, "SmartPlanner Email Configuration");
    } catch (IOException e) {
      System.err.println("Error saving email config: " + e.getMessage());
    }
  }

  // Getters and setters
  public String getSmtpHost() {
    return smtpHost;
  }

  public void setSmtpHost(String smtpHost) {
    this.smtpHost = smtpHost;
  }

  public int getSmtpPort() {
    return smtpPort;
  }

  public void setSmtpPort(int smtpPort) {
    this.smtpPort = smtpPort;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFromEmail() {
    return fromEmail;
  }

  public void setFromEmail(String fromEmail) {
    this.fromEmail = fromEmail;
  }

  public String getToEmail() {
    return toEmail;
  }

  public void setToEmail(String toEmail) {
    this.toEmail = toEmail;
  }

  public boolean isUseTLS() {
    return useTLS;
  }

  public void setUseTLS(boolean useTLS) {
    this.useTLS = useTLS;
  }

  /**
   * Checks if the configuration is valid and complete.
   */
  public boolean isValid() {
    return smtpHost != null && !smtpHost.isEmpty() &&
           username != null && !username.isEmpty() &&
           password != null && !password.isEmpty() &&
           fromEmail != null && !fromEmail.isEmpty() &&
           toEmail != null && !toEmail.isEmpty();
  }
}
