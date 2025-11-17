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
  private String category;

  public Task() {
    this.completed = false;
    this.priority = "Medium";
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

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
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
        category != null ? category : "");
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