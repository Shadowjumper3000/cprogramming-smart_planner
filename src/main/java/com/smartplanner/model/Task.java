package com.smartplanner.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a task in the planner.
 */
public class Task {
  private String id;
  private String title;
  private String description;
  private LocalDate dueDate;
  private LocalTime dueTime;
  private String priority; // High, Medium, Low
  private boolean completed;
  private boolean isLate;
  private String category;
  private boolean emailReminderEnabled;
  private int reminderMinutesBefore; // Minutes before due time to send reminder

  public Task() {
    this.completed = false;
    this.isLate = false;
    this.priority = "Medium";
    this.emailReminderEnabled = false;
    this.reminderMinutesBefore = 30; // Default 30 minutes before
  }

  public Task(String id, String title, String description, LocalDate dueDate,
      LocalTime dueTime, String priority, boolean completed, String category) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.dueDate = dueDate;
    this.dueTime = dueTime;
    this.priority = priority;
    this.completed = completed;
    this.isLate = false;
    this.category = category;
  }

  // Getters and setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public LocalTime getDueTime() {
    return dueTime;
  }

  public void setDueTime(LocalTime dueTime) {
    this.dueTime = dueTime;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public boolean isLate() {return isLate;}

    public void setLate(boolean isLate) {this.isLate = isLate;}

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public boolean isEmailReminderEnabled() {
    return emailReminderEnabled;
  }

  public void setEmailReminderEnabled(boolean emailReminderEnabled) {
    this.emailReminderEnabled = emailReminderEnabled;
  }

  public int getReminderMinutesBefore() {
    return reminderMinutesBefore;
  }

  public void setReminderMinutesBefore(int reminderMinutesBefore) {
    this.reminderMinutesBefore = reminderMinutesBefore;
  }

  /**
   * Converts the task to CSV format for file storage.
   */
  public String toCsvString() {
    return String.join(",",
        id != null ? id : "",
        title != null ? title : "",
        description != null ? description : "",
        dueDate != null ? dueDate.toString() : "",
        dueTime != null ? dueTime.toString() : "",
        priority != null ? priority : "Medium",
        String.valueOf(completed),
        category != null ? category : "",
        String.valueOf(emailReminderEnabled),
        String.valueOf(reminderMinutesBefore));
  }

  /**
   * Creates a Task from CSV string.
   */
  public static Task fromCsvString(String csvLine) {
    String[] parts = csvLine.split(",", -1); // -1 to keep empty strings
    if (parts.length < 8) {
      return null;
    }

    Task task = new Task();
    task.setId(parts[0].trim());
    task.setTitle(parts[1].trim());
    task.setDescription(parts[2].trim());

    if (!parts[3].trim().isEmpty()) {
      task.setDueDate(LocalDate.parse(parts[3].trim()));
    }

    if (!parts[4].trim().isEmpty()) {
      task.setDueTime(LocalTime.parse(parts[4].trim()));
    }

    task.setPriority(parts[5].trim().isEmpty() ? "Medium" : parts[5].trim());
    task.setCompleted(Boolean.parseBoolean(parts[6].trim()));
    task.setCategory(parts[7].trim());

    // Load reminder fields if present (for backwards compatibility)
    if (parts.length > 8 && !parts[8].trim().isEmpty()) {
      task.setEmailReminderEnabled(Boolean.parseBoolean(parts[8].trim()));
    }
    if (parts.length > 9 && !parts[9].trim().isEmpty()) {
      task.setReminderMinutesBefore(Integer.parseInt(parts[9].trim()));
    }

    return task;
  }

  @Override
  public String toString() {
    return "Task{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", dueDate=" + dueDate +
        ", dueTime=" + dueTime +
        ", priority='" + priority + '\'' +
        ", completed=" + completed +
        ", category='" + category + '\'' +
        '}';
  }
}