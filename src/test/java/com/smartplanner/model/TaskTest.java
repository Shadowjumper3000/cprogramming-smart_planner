package com.smartplanner.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Model Tests")
class TaskTest {

  private Task task;

  @BeforeEach
  void setUp() {
    task = new Task();
  }

  @Test
  @DisplayName("Should create task with default values")
  void testDefaultConstructor() {
    assertNotNull(task);
    assertFalse(task.isCompleted());
    assertFalse(task.isLate());
    assertEquals("Medium", task.getPriority());
  }

  @Test
  @DisplayName("Should create task with all parameters")
  void testParameterizedConstructor() {
    LocalDate dueDate = LocalDate.of(2025, 12, 15);
    LocalTime dueTime = LocalTime.of(14, 30);

    Task task = new Task("TASK_001", "Test Task", "Test Description",
        dueDate, dueTime, "High", false, "Work");

    assertEquals("TASK_001", task.getId());
    assertEquals("Test Task", task.getTitle());
    assertEquals("Test Description", task.getDescription());
    assertEquals(dueDate, task.getDueDate());
    assertEquals(dueTime, task.getDueTime());
    assertEquals("High", task.getPriority());
    assertFalse(task.isCompleted());
    assertEquals("Work", task.getCategory());
  }

  @Test
  @DisplayName("Should set and get all properties correctly")
  void testGettersAndSetters() {
    LocalDate dueDate = LocalDate.of(2025, 12, 20);
    LocalTime dueTime = LocalTime.of(10, 0);

    task.setId("TASK_123");
    task.setTitle("My Task");
    task.setDescription("Task description");
    task.setDueDate(dueDate);
    task.setDueTime(dueTime);
    task.setPriority("Low");
    task.setCompleted(true);
    task.setLate(true);
    task.setCategory("Personal");

    assertEquals("TASK_123", task.getId());
    assertEquals("My Task", task.getTitle());
    assertEquals("Task description", task.getDescription());
    assertEquals(dueDate, task.getDueDate());
    assertEquals(dueTime, task.getDueTime());
    assertEquals("Low", task.getPriority());
    assertTrue(task.isCompleted());
    assertTrue(task.isLate());
    assertEquals("Personal", task.getCategory());
  }

  @Test
  @DisplayName("Should convert task to CSV string correctly")
  void testToCsvString() {
    task.setId("TASK_001");
    task.setTitle("Test");
    task.setDescription("Description");
    task.setDueDate(LocalDate.of(2025, 12, 15));
    task.setDueTime(LocalTime.of(14, 30));
    task.setPriority("High");
    task.setCompleted(false);
    task.setCategory("Work");

    String csv = task.toCsvString();
    assertEquals("TASK_001,Test,Description,2025-12-15,14:30,High,false,Work,false,30", csv);
  }

  @Test
  @DisplayName("Should convert task with null values to CSV string")
  void testToCsvStringWithNullValues() {
    task.setId("TASK_002");
    task.setTitle("Test");

    String csv = task.toCsvString();
    assertEquals("TASK_002,Test,,,,Medium,false,,false,30", csv);
  }

  @Test
  @DisplayName("Should create task from CSV string")
  void testFromCsvString() {
    String csvLine = "TASK_001,Test Task,Description,2025-12-15,14:30,High,false,Work";
    Task task = Task.fromCsvString(csvLine);

    assertNotNull(task);
    assertEquals("TASK_001", task.getId());
    assertEquals("Test Task", task.getTitle());
    assertEquals("Description", task.getDescription());
    assertEquals(LocalDate.of(2025, 12, 15), task.getDueDate());
    assertEquals(LocalTime.of(14, 30), task.getDueTime());
    assertEquals("High", task.getPriority());
    assertFalse(task.isCompleted());
    assertEquals("Work", task.getCategory());
  }

  @Test
  @DisplayName("Should create task from CSV with empty fields")
  void testFromCsvStringWithEmptyFields() {
    String csvLine = "TASK_002,Test,,,,Medium,true,";
    Task task = Task.fromCsvString(csvLine);

    assertNotNull(task);
    assertEquals("TASK_002", task.getId());
    assertEquals("Test", task.getTitle());
    assertEquals("", task.getDescription());
    assertNull(task.getDueDate());
    assertNull(task.getDueTime());
    assertEquals("Medium", task.getPriority());
    assertTrue(task.isCompleted());
    assertEquals("", task.getCategory());
  }

  @Test
  @DisplayName("Should return null for invalid CSV string")
  void testFromCsvStringInvalid() {
    String csvLine = "TASK_001,Test";
    Task task = Task.fromCsvString(csvLine);

    assertNull(task);
  }

  @Test
  @DisplayName("Should have valid toString method")
  void testToString() {
    task.setId("TASK_001");
    task.setTitle("Test");

    String result = task.toString();
    assertTrue(result.contains("TASK_001"));
    assertTrue(result.contains("Test"));
  }
}
