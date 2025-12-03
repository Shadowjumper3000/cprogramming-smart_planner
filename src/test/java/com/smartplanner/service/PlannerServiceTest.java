package com.smartplanner.service;

import com.smartplanner.model.Task;
import com.smartplanner.model.TaskReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlannerService Tests")
class PlannerServiceTest {

  @TempDir
  Path tempDir;

  private PlannerService plannerService;

  @BeforeEach
  void setUp() throws IOException {
    // Create a temporary data directory and planner file
    File dataDir = tempDir.resolve("data").toFile();
    dataDir.mkdirs();

    File plannerFile = new File(dataDir, "planner.csv");
    plannerFile.createNewFile();

    // Set system property to use temp directory
    System.setProperty("user.dir", tempDir.toString());

    plannerService = new PlannerService();

    // Clear any existing tasks to ensure test isolation
    plannerService.getAllTasks().forEach(task -> plannerService.deleteTask(task.getId()));
  }

  @Test
  @DisplayName("Should initialize with empty task list")
  void testInitialization() {
    List<Task> tasks = plannerService.getAllTasks();
    assertNotNull(tasks);
  }

  @Test
  @DisplayName("Should add task successfully")
  void testAddTask() {
    Task task = new Task();
    task.setTitle("Test Task");
    task.setDescription("Test Description");
    task.setPriority("High");

    plannerService.addTask(task);

    List<Task> tasks = plannerService.getAllTasks();
    assertEquals(1, tasks.size());
    assertNotNull(tasks.get(0).getId());
  }

  @Test
  @DisplayName("Should update existing task")
  void testUpdateTask() {
    Task task = new Task();
    task.setTitle("Original Title");
    plannerService.addTask(task);

    String taskId = plannerService.getAllTasks().get(0).getId();

    Task updatedTask = new Task();
    updatedTask.setId(taskId);
    updatedTask.setTitle("Updated Title");
    updatedTask.setDescription("Updated Description");

    plannerService.updateTask(updatedTask);

    List<Task> tasks = plannerService.getAllTasks();
    assertEquals(1, tasks.size());
    assertEquals("Updated Title", tasks.get(0).getTitle());
  }

  @Test
  @DisplayName("Should delete task by ID")
  void testDeleteTask() {
    Task task = new Task();
    task.setTitle("Task to Delete");
    plannerService.addTask(task);

    String taskId = plannerService.getAllTasks().get(0).getId();
    assertEquals(1, plannerService.getAllTasks().size());

    plannerService.deleteTask(taskId);

    assertEquals(0, plannerService.getAllTasks().size());
  }

  @Test
  @DisplayName("Should filter tasks by completion status")
  void testGetTasksByCompletion() {
    Task completedTask = new Task();
    completedTask.setTitle("Completed");
    completedTask.setCompleted(true);
    plannerService.addTask(completedTask);

    Task pendingTask = new Task();
    pendingTask.setTitle("Pending");
    pendingTask.setCompleted(false);
    plannerService.addTask(pendingTask);

    List<Task> completed = plannerService.getTasksByCompletion(true);
    List<Task> pending = plannerService.getTasksByCompletion(false);

    assertEquals(1, completed.size());
    assertEquals(1, pending.size());
    assertEquals("Completed", completed.get(0).getTitle());
    assertEquals("Pending", pending.get(0).getTitle());
  }

  @Test
  @DisplayName("Should filter tasks by priority")
  void testGetTasksByPriority() {
    Task highTask = new Task();
    highTask.setTitle("High Priority");
    highTask.setPriority("High");
    plannerService.addTask(highTask);

    Task mediumTask = new Task();
    mediumTask.setTitle("Medium Priority");
    mediumTask.setPriority("Medium");
    plannerService.addTask(mediumTask);

    List<Task> highPriority = plannerService.getTasksByPriority("High");

    assertEquals(1, highPriority.size());
    assertEquals("High Priority", highPriority.get(0).getTitle());
  }

  @Test
  @DisplayName("Should filter tasks by category")
  void testGetTasksByCategory() {
    Task workTask = new Task();
    workTask.setTitle("Work Task");
    workTask.setCategory("Work");
    plannerService.addTask(workTask);

    Task personalTask = new Task();
    personalTask.setTitle("Personal Task");
    personalTask.setCategory("Personal");
    plannerService.addTask(personalTask);

    List<Task> workTasks = plannerService.getTasksByCategory("Work");

    assertEquals(1, workTasks.size());
    assertEquals("Work Task", workTasks.get(0).getTitle());
  }

  @Test
  @DisplayName("Should get tasks due today")
  void testGetTasksDueToday() {
    Task todayTask = new Task();
    todayTask.setTitle("Due Today");
    todayTask.setDueDate(LocalDate.now());
    plannerService.addTask(todayTask);

    Task tomorrowTask = new Task();
    tomorrowTask.setTitle("Due Tomorrow");
    tomorrowTask.setDueDate(LocalDate.now().plusDays(1));
    plannerService.addTask(tomorrowTask);

    List<Task> todayTasks = plannerService.getTasksDueToday();

    assertEquals(1, todayTasks.size());
    assertEquals("Due Today", todayTasks.get(0).getTitle());
  }

  @Test
  @DisplayName("Should get overdue tasks")
  void testGetOverdueTasks() {
    Task overdueTask = new Task();
    overdueTask.setTitle("Overdue Task");
    overdueTask.setDueDate(LocalDate.now().minusDays(1));
    overdueTask.setCompleted(false);
    plannerService.addTask(overdueTask);

    Task futureTask = new Task();
    futureTask.setTitle("Future Task");
    futureTask.setDueDate(LocalDate.now().plusDays(1));
    futureTask.setCompleted(false);
    plannerService.addTask(futureTask);

    List<Task> overdueTasks = plannerService.getOverdueTasks();

    assertEquals(1, overdueTasks.size());
    assertEquals("Overdue Task", overdueTasks.get(0).getTitle());
  }

  @Test
  @DisplayName("Should search tasks by title")
  void testSearchTasks() {
    Task task1 = new Task();
    task1.setTitle("Java Programming");
    task1.setDescription("Learn Java");
    plannerService.addTask(task1);

    Task task2 = new Task();
    task2.setTitle("Python Course");
    task2.setDescription("Study Python");
    plannerService.addTask(task2);

    List<Task> results = plannerService.searchTasks("java");

    assertEquals(1, results.size());
    assertEquals("Java Programming", results.get(0).getTitle());
  }

  @Test
  @DisplayName("Should search tasks by description")
  void testSearchTasksByDescription() {
    Task task = new Task();
    task.setTitle("Project");
    task.setDescription("Complete final project for Java course");
    plannerService.addTask(task);

    List<Task> results = plannerService.searchTasks("final");

    assertEquals(1, results.size());
  }

  @Test
  @DisplayName("Should get all unique categories")
  void testGetAllCategories() {
    Task task1 = new Task();
    task1.setCategory("Work");
    plannerService.addTask(task1);

    Task task2 = new Task();
    task2.setCategory("Personal");
    plannerService.addTask(task2);

    Task task3 = new Task();
    task3.setCategory("Work");
    plannerService.addTask(task3);

    Set<String> categories = plannerService.getAllCategories();

    assertEquals(2, categories.size());
    assertTrue(categories.contains("Work"));
    assertTrue(categories.contains("Personal"));
  }

  @Test
  @DisplayName("Should generate task report")
  void testGenerateReport() {
    Task pending = new Task();
    pending.setCompleted(false);
    pending.setDueDate(LocalDate.now().plusDays(1));
    plannerService.addTask(pending);

    Task late = new Task();
    late.setCompleted(false);
    late.setDueDate(LocalDate.now().minusDays(1));
    plannerService.addTask(late);

    Task completed = new Task();
    completed.setCompleted(true);
    plannerService.addTask(completed);

    TaskReport report = plannerService.generateReport();

    assertNotNull(report);
    assertTrue(report.getPendingTasks().size() >= 1);
    assertTrue(report.getLateTasks().size() >= 1);
    assertTrue(report.getCompletedTasks().size() >= 1);
  }

  @Test
  @DisplayName("Should convert tasks to table data")
  void testGetTasksAsTableData() {
    Task task = new Task();
    task.setTitle("Test");
    task.setDescription("Description");
    task.setDueDate(LocalDate.of(2025, 12, 15));
    task.setDueTime(LocalTime.of(10, 0));
    task.setPriority("High");
    task.setCompleted(false);
    task.setCategory("Work");
    plannerService.addTask(task);

    String[][] tableData = plannerService.getTasksAsTableData();

    assertEquals(1, tableData.length);
    assertEquals("Test", tableData[0][0]);
    assertEquals("Description", tableData[0][1]);
  }

  @Test
  @DisplayName("Should get table headers")
  void testGetTableHeaders() {
    String[] headers = plannerService.getTableHeaders();

    assertEquals(8, headers.length);
    assertEquals("Title", headers[0]);
    assertEquals("Description", headers[1]);
    assertEquals("Due Date", headers[2]);
  }
}
