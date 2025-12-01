package com.smartplanner.service;

import com.smartplanner.model.Task;
import com.smartplanner.model.TaskReport;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing planner tasks.
 */
public class PlannerService {
  private static final String PLANNER_FILE = "data/planner.csv";
  private final List<Task> tasks;

  public PlannerService() {
    this.tasks = new ArrayList<>();
    loadTasks();
  }

  /**
   * Loads tasks from the CSV file.
   */
  public void loadTasks() {
    tasks.clear();
    File file = new File(PLANNER_FILE);

    if (!file.exists()) {
      return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          Task task = Task.fromCsvString(line);
          if (task != null) {
            tasks.add(task);
          }
        }
      }
    } catch (IOException e) {
      System.err.println("Error loading tasks from file: " + e.getMessage());
    }
  }

  /**
   * Saves all tasks to the CSV file.
   */
  public void saveTasks() {
    try (FileWriter writer = new FileWriter(PLANNER_FILE)) {
      for (Task task : tasks) {
        writer.write(task.toCsvString() + "\n");
      }
    } catch (IOException e) {
      System.err.println("Error saving tasks to file: " + e.getMessage());
    }
  }

  /**
   * Adds a new task.
   */
  public void addTask(Task task) {
    if (task.getId() == null || task.getId().isEmpty()) {
      task.setId(generateTaskId());
    }
    tasks.add(task);
    saveTasks();
  }

  /**
   * Updates an existing task.
   */
  public void updateTask(Task updatedTask) {
    for (int i = 0; i < tasks.size(); i++) {
      if (tasks.get(i).getId().equals(updatedTask.getId())) {
        tasks.set(i, updatedTask);
        saveTasks();
        return;
      }
    }
  }

  /**
   * Deletes a task by ID.
   */
  public void deleteTask(String taskId) {
    tasks.removeIf(task -> task.getId().equals(taskId));
    saveTasks();
  }

  /**
   * Gets all tasks.
   */
  public List<Task> getAllTasks() {
    return new ArrayList<>(tasks);
  }

  /**
   * Gets tasks filtered by completion status.
   */
  public List<Task> getTasksByCompletion(boolean completed) {
    return tasks.stream()
        .filter(task -> task.isCompleted() == completed)
        .collect(Collectors.toList());
  }

  /**
   * Gets tasks filtered by priority.
   */
  public List<Task> getTasksByPriority(String priority) {
    return tasks.stream()
        .filter(task -> priority.equals(task.getPriority()))
        .collect(Collectors.toList());
  }

  /**
   * Gets tasks filtered by category.
   */
  public List<Task> getTasksByCategory(String category) {
    return tasks.stream()
        .filter(task -> category.equals(task.getCategory()))
        .collect(Collectors.toList());
  }

  /**
   * Gets tasks due today.
   */
  public List<Task> getTasksDueToday() {
    LocalDate today = LocalDate.now();
    return tasks.stream()
        .filter(task -> today.equals(task.getDueDate()))
        .collect(Collectors.toList());
  }

  /**
   * Gets overdue tasks.
   */
  public List<Task> getOverdueTasks() {
    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();

    return tasks.stream()
        .filter(task -> !task.isCompleted() && task.getDueDate() != null)
        .filter(task -> task.getDueDate().isBefore(today) ||
            (task.getDueDate().equals(today) && task.getDueTime() != null && task.getDueTime().isBefore(now)))
        .collect(Collectors.toList());
  }

  /**
   * Converts tasks to 2D array for table display.
   */
  public String[][] getTasksAsTableData() {
    String[][] data = new String[tasks.size()][8];

    for (int i = 0; i < tasks.size(); i++) {
      Task task = tasks.get(i);
      data[i][0] = task.getTitle() != null ? task.getTitle() : "";
      data[i][1] = task.getDescription() != null ? task.getDescription() : "";
      data[i][2] = task.getDueDate() != null ? task.getDueDate().toString() : "";
      data[i][3] = task.getDueTime() != null ? task.getDueTime().toString() : "";
      data[i][4] = task.getPriority() != null ? task.getPriority() : "Medium";
      data[i][5] = String.valueOf(task.isCompleted());
      data[i][6] = task.getCategory() != null ? task.getCategory() : "";
      data[i][7] = task.getId() != null ? task.getId() : "";
    }

    return data;
  }

  /**
   * Gets table column headers for task display.
   */
  public String[] getTableHeaders() {
    return new String[] { "Title", "Description", "Due Date", "Due Time", "Priority", "Completed", "Category", "ID" };
  }

  /**
   * Generates a unique task ID.
   */
  private String generateTaskId() {
    return "TASK_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000);
  }

  /**
   * Searches tasks by title or description.
   */
  public List<Task> searchTasks(String searchTerm) {
    String lowerSearchTerm = searchTerm.toLowerCase();
    return tasks.stream()
        .filter(task -> (task.getTitle() != null && task.getTitle().toLowerCase().contains(lowerSearchTerm)) ||
            (task.getDescription() != null && task.getDescription().toLowerCase().contains(lowerSearchTerm)))
        .collect(Collectors.toList());
  }

  /**
   * Gets unique categories from all tasks.
   */
  public Set<String> getAllCategories() {
    return tasks.stream()
        .map(Task::getCategory)
        .filter(Objects::nonNull)
        .filter(category -> !category.trim().isEmpty())
        .collect(Collectors.toSet());
  }

    public TaskReport generateReport() {
        List<Task> pending = getTasksByCompletion(false);
        List<Task> late = getOverdueTasks();
        List<Task> completed = getTasksByCompletion(true);

        return new TaskReport(pending, late, completed);
    }

}