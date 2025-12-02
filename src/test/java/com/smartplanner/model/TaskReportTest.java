package com.smartplanner.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskReport Model Tests")
class TaskReportTest {

  private List<Task> pendingTasks;
  private List<Task> lateTasks;
  private List<Task> completedTasks;
  private TaskReport report;

  @BeforeEach
  void setUp() {
    pendingTasks = new ArrayList<>();
    lateTasks = new ArrayList<>();
    completedTasks = new ArrayList<>();

    // Create sample tasks
    Task pendingTask = new Task("T1", "Pending Task", "Description",
        LocalDate.now().plusDays(1), LocalTime.of(10, 0),
        "High", false, "Work");
    pendingTasks.add(pendingTask);

    Task lateTask = new Task("T2", "Late Task", "Description",
        LocalDate.now().minusDays(1), LocalTime.of(10, 0),
        "High", false, "Work");
    lateTasks.add(lateTask);

    Task completedTask = new Task("T3", "Completed Task", "Description",
        LocalDate.now(), LocalTime.of(10, 0),
        "Medium", true, "Personal");
    completedTasks.add(completedTask);

    report = new TaskReport(pendingTasks, lateTasks, completedTasks);
  }

  @Test
  @DisplayName("Should create report with all task lists")
  void testConstructor() {
    assertNotNull(report);
    assertEquals(1, report.getPendingTasks().size());
    assertEquals(1, report.getLateTasks().size());
    assertEquals(1, report.getCompletedTasks().size());
    assertEquals(LocalDate.now(), report.getReportGeneratedDate());
  }

  @Test
  @DisplayName("Should return correct pending tasks")
  void testGetPendingTasks() {
    List<Task> pending = report.getPendingTasks();
    assertEquals(1, pending.size());
    assertEquals("Pending Task", pending.get(0).getTitle());
  }

  @Test
  @DisplayName("Should return correct late tasks")
  void testGetLateTasks() {
    List<Task> late = report.getLateTasks();
    assertEquals(1, late.size());
    assertEquals("Late Task", late.get(0).getTitle());
  }

  @Test
  @DisplayName("Should return correct completed tasks")
  void testGetCompletedTasks() {
    List<Task> completed = report.getCompletedTasks();
    assertEquals(1, completed.size());
    assertEquals("Completed Task", completed.get(0).getTitle());
  }

  @Test
  @DisplayName("Should calculate total tasks correctly")
  void testGetTotalTasks() {
    assertEquals(3, report.getTotalTasks());
  }

  @Test
  @DisplayName("Should calculate completion percentage correctly")
  void testGetCompletionPercentage() {
    assertEquals(33, report.getCompletionPercentage()); // 1/3 = 33%
  }

  @Test
  @DisplayName("Should handle empty task lists")
  void testEmptyTaskLists() {
    TaskReport emptyReport = new TaskReport(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    assertEquals(0, emptyReport.getTotalTasks());
    assertEquals(0, emptyReport.getCompletionPercentage());
  }

  @Test
  @DisplayName("Should calculate 100% completion")
  void testFullCompletion() {
    List<Task> allCompleted = new ArrayList<>();
    allCompleted.add(new Task("T1", "Task 1", "", null, null, "Medium", true, ""));
    allCompleted.add(new Task("T2", "Task 2", "", null, null, "Medium", true, ""));

    TaskReport fullReport = new TaskReport(new ArrayList<>(), new ArrayList<>(), allCompleted);
    assertEquals(100, fullReport.getCompletionPercentage());
  }

  @Test
  @DisplayName("Should calculate 0% completion")
  void testZeroCompletion() {
    List<Task> noneCompleted = new ArrayList<>();
    noneCompleted.add(new Task("T1", "Task 1", "", null, null, "Medium", false, ""));

    TaskReport zeroReport = new TaskReport(noneCompleted, new ArrayList<>(), new ArrayList<>());
    assertEquals(0, zeroReport.getCompletionPercentage());
  }
}
