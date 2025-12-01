package com.smartplanner.service;

import com.smartplanner.model.Task;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service for managing and sending task reminders.
 */
public class ReminderService {
  private final PlannerService plannerService;
  private final EmailService emailService;
  private final ScheduledExecutorService scheduler;
  private final Set<String> sentReminders; // Track sent reminders to avoid duplicates
  private boolean isRunning;

  public ReminderService(PlannerService plannerService, EmailService emailService) {
    this.plannerService = plannerService;
    this.emailService = emailService;
    this.scheduler = Executors.newScheduledThreadPool(1);
    this.sentReminders = new HashSet<>();
    this.isRunning = false;
  }

  /**
   * Starts the reminder service to check for tasks periodically.
   */
  public void start() {
    if (isRunning) {
      System.out.println("Reminder service is already running.");
      return;
    }

    isRunning = true;
    // Check for reminders every minute
    scheduler.scheduleAtFixedRate(this::checkAndSendReminders, 0, 1, TimeUnit.MINUTES);
    System.out.println("Reminder service started.");
  }

  /**
   * Stops the reminder service.
   */
  public void stop() {
    if (!isRunning) {
      return;
    }

    isRunning = false;
    scheduler.shutdown();
    try {
      if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
        scheduler.shutdownNow();
      }
    } catch (InterruptedException e) {
      scheduler.shutdownNow();
      Thread.currentThread().interrupt();
    }
    System.out.println("Reminder service stopped.");
  }

  /**
   * Checks all tasks and sends reminders for those that are due soon or at deadline.
   */
  private void checkAndSendReminders() {
    try {
      // Reload tasks from file to get latest changes
      plannerService.loadTasks();
      List<Task> tasks = plannerService.getAllTasks();
      LocalDateTime now = LocalDateTime.now();
      
      System.out.println("Checking reminders at: " + now + " | Total tasks: " + tasks.size());

      for (Task task : tasks) {
        // Skip if task is completed or reminder is not enabled
        if (task.isCompleted() || !task.isEmailReminderEnabled()) {
          continue;
        }

        // Skip if task has no due date/time
        if (task.getDueDate() == null || task.getDueTime() == null) {
          continue;
        }

        // Calculate when the task is due
        LocalDateTime taskDueTime = LocalDateTime.of(task.getDueDate(), task.getDueTime());
        
        System.out.println("Task: " + task.getTitle() + " | Due: " + taskDueTime + " | Reminder enabled: " + task.isEmailReminderEnabled());
        
        // Check 1: Send advance reminder (before deadline)
        LocalDateTime reminderTime = taskDueTime.minusMinutes(task.getReminderMinutesBefore());
        long minutesUntilReminder = ChronoUnit.MINUTES.between(now, reminderTime);
        String reminderKey = task.getId() + "_reminder_" + reminderTime.toString();
        
        System.out.println("  Advance reminder: " + minutesUntilReminder + " minutes until reminder time");
        
        if (minutesUntilReminder <= 0 && 
            minutesUntilReminder > -60 && // Don't send if more than 1 hour past reminder time
            !sentReminders.contains(reminderKey) &&
            taskDueTime.isAfter(now)) {
          
          System.out.println("  -> Sending UPCOMING reminder!");
          sendReminder(task, "UPCOMING");
          sentReminders.add(reminderKey);
        }
        
        // Check 2: Send deadline reminder (when deadline is hit)
        long minutesUntilDeadline = ChronoUnit.MINUTES.between(now, taskDueTime);
        String deadlineKey = task.getId() + "_deadline_" + taskDueTime.toString();
        
        System.out.println("  Deadline check: " + minutesUntilDeadline + " minutes until deadline | Already sent: " + sentReminders.contains(deadlineKey));
        
        if (minutesUntilDeadline <= 0 && 
            minutesUntilDeadline > -60 && // Send within 1 hour after deadline
            !sentReminders.contains(deadlineKey)) {
          
          System.out.println("  -> Sending DEADLINE reminder!");
          sendReminder(task, "DEADLINE");
          sentReminders.add(deadlineKey);
        }
      }

      // Clean up old sent reminders (older than 24 hours)
      cleanupSentReminders();

    } catch (Exception e) {
      System.err.println("Error checking reminders: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Sends a reminder for a specific task.
   */
  private void sendReminder(Task task, String reminderType) {
    try {
      boolean success = emailService.sendTaskReminder(task, reminderType);
      if (success) {
        System.out.println(reminderType + " reminder sent for task: " + task.getTitle());
      } else {
        System.err.println("Failed to send " + reminderType + " reminder for task: " + task.getTitle());
      }
    } catch (Exception e) {
      System.err.println("Error sending " + reminderType + " reminder for task " + task.getTitle() + ": " + e.getMessage());
    }
  }

  /**
   * Manually sends a reminder for a specific task.
   */
  public boolean sendReminderNow(Task task) {
    if (task == null) {
      return false;
    }
    return emailService.sendTaskReminder(task);
  }

  /**
   * Cleans up the sent reminders set to prevent memory leaks.
   * Keeps only reminders from the last 24 hours.
   */
  private void cleanupSentReminders() {
    // For simplicity, clear all if size exceeds 1000
    // In production, you'd want to parse timestamps and remove old ones
    if (sentReminders.size() > 1000) {
      sentReminders.clear();
    }
  }

  /**
   * Gets tasks that have upcoming reminders.
   */
  public List<Task> getUpcomingReminders() {
    List<Task> upcomingReminders = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime next24Hours = now.plusHours(24);

    for (Task task : plannerService.getAllTasks()) {
      if (task.isCompleted() || !task.isEmailReminderEnabled()) {
        continue;
      }

      if (task.getDueDate() == null || task.getDueTime() == null) {
        continue;
      }

      LocalDateTime taskDueTime = LocalDateTime.of(task.getDueDate(), task.getDueTime());
      LocalDateTime reminderTime = taskDueTime.minusMinutes(task.getReminderMinutesBefore());

      if (reminderTime.isAfter(now) && reminderTime.isBefore(next24Hours)) {
        upcomingReminders.add(task);
      }
    }

    // Sort by reminder time
    upcomingReminders.sort((t1, t2) -> {
      LocalDateTime r1 = LocalDateTime.of(t1.getDueDate(), t1.getDueTime())
          .minusMinutes(t1.getReminderMinutesBefore());
      LocalDateTime r2 = LocalDateTime.of(t2.getDueDate(), t2.getDueTime())
          .minusMinutes(t2.getReminderMinutesBefore());
      return r1.compareTo(r2);
    });

    return upcomingReminders;
  }

  public boolean isRunning() {
    return isRunning;
  }

  /**
   * Resets the sent reminders tracking (useful for testing).
   */
  public void resetSentReminders() {
    sentReminders.clear();
  }
}
