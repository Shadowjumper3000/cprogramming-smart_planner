package com.smartplanner.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DataUtils Tests")
class DataUtilsTest {

  @TempDir
  Path tempDir;

  private File csvFile;

  @BeforeEach
  void setUp() throws IOException {
    csvFile = tempDir.resolve("test.csv").toFile();
  }

  @Test
  @DisplayName("Should load CSV data correctly")
  void testLoadCsvData() throws IOException {
    // Write test CSV data
    try (FileWriter writer = new FileWriter(csvFile)) {
      writer.write("Time,Monday,Tuesday\n");
      writer.write("09:00,Math,Physics\n");
      writer.write("10:00,English,Chemistry\n");
    }

    String[][] data = DataUtils.loadCsvData(csvFile.getAbsolutePath());

    assertNotNull(data);
    assertEquals(3, data.length); // 3 rows including header
    assertEquals(3, data[0].length); // 3 columns
    assertEquals("Time", data[0][0]);
    assertEquals("09:00", data[1][0]);
  }

  @Test
  @DisplayName("Should handle empty CSV file")
  void testLoadEmptyCsvFile() throws IOException {
    csvFile.createNewFile();

    String[][] data = DataUtils.loadCsvData(csvFile.getAbsolutePath());

    assertNotNull(data);
    assertEquals(0, data.length);
  }

  @Test
  @DisplayName("Should handle non-existent file")
  void testLoadNonExistentFile() {
    String[][] data = DataUtils.loadCsvData("nonexistent.csv");

    assertNotNull(data);
    assertEquals(0, data.length);
  }

  @Test
  @DisplayName("Should extract data from JTable")
  void testGetTableData() {
    String[] columnNames = { "Name", "Age", "City" };
    Object[][] tableData = {
        { "Alice", "25", "New York" },
        { "Bob", "30", "London" },
        { "Charlie", "35", "Paris" }
    };

    DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
    JTable table = new JTable(model);

    String[][] data = DataUtils.getTableData(table);

    assertEquals(3, data.length);
    assertEquals(3, data[0].length);
    assertEquals("Alice", data[0][0]);
    assertEquals("25", data[0][1]);
    assertEquals("Paris", data[2][2]);
  }

  @Test
  @DisplayName("Should handle empty JTable")
  void testGetTableDataEmpty() {
    String[] columnNames = { "Name", "Age" };
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    JTable table = new JTable(model);

    String[][] data = DataUtils.getTableData(table);

    assertEquals(0, data.length);
  }

  @Test
  @DisplayName("Should handle null values in JTable")
  void testGetTableDataWithNulls() {
    String[] columnNames = { "Name", "Age" };
    Object[][] tableData = {
        { "Alice", null },
        { null, "30" }
    };

    DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
    JTable table = new JTable(model);

    String[][] data = DataUtils.getTableData(table);

    assertEquals(2, data.length);
    assertEquals("Alice", data[0][0]);
    assertEquals("", data[0][1]); // null converted to empty string
    assertEquals("", data[1][0]);
    assertEquals("30", data[1][1]);
  }

  @Test
  @DisplayName("Should save CSV data correctly")
  void testSaveCsvData() throws IOException {
    String[][] data = {
        { "Name", "Age", "City" },
        { "Alice", "25", "New York" },
        { "Bob", "30", "London" }
    };

    DataUtils.saveCsvData(data, csvFile.getAbsolutePath());

    assertTrue(csvFile.exists());
    assertTrue(csvFile.length() > 0);
  }

  @Test
  @DisplayName("Should save table data to CSV")
  void testSaveTableData() throws IOException {
    String[] columnNames = { "Name", "Age" };
    Object[][] tableData = {
        { "Alice", "25" },
        { "Bob", "30" }
    };

    DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
    JTable table = new JTable(model);

    DataUtils.saveTableData(table, csvFile.getAbsolutePath());

    assertTrue(csvFile.exists());
    assertTrue(csvFile.length() > 0);
  }

  @Test
  @DisplayName("Should sort table by time")
  void testSortTableByTime() {
    String[][] unsorted = {
        { "14:30", "Lunch", "Break" },
        { "09:00", "Meeting", "Standup" },
        { "11:45", "Review", "Planning" }
    };

    String[][] sorted = DataUtils.sortTableByTime(unsorted);

    assertEquals("09:00", sorted[0][0]);
    assertEquals("11:45", sorted[1][0]);
    assertEquals("14:30", sorted[2][0]);
  }

  @Test
  @DisplayName("Should handle already sorted table")
  void testSortAlreadySortedTable() {
    String[][] alreadySorted = {
        { "08:00", "Early", "Meeting" },
        { "10:00", "Mid", "Morning" },
        { "15:00", "Late", "Afternoon" }
    };

    String[][] sorted = DataUtils.sortTableByTime(alreadySorted);

    assertEquals("08:00", sorted[0][0]);
    assertEquals("10:00", sorted[1][0]);
    assertEquals("15:00", sorted[2][0]);
  }

  @Test
  @DisplayName("Should handle single row table")
  void testSortSingleRowTable() {
    String[][] singleRow = {
        { "10:00", "Only", "One" }
    };

    String[][] sorted = DataUtils.sortTableByTime(singleRow);

    assertEquals(1, sorted.length);
    assertEquals("10:00", sorted[0][0]);
  }

  @Test
  @DisplayName("Should handle empty table in sort")
  void testSortEmptyTable() {
    String[][] empty = {};

    String[][] sorted = DataUtils.sortTableByTime(empty);

    assertEquals(0, sorted.length);
  }

  @Test
  @DisplayName("Should handle invalid time format gracefully")
  void testSortInvalidTimeFormat() {
    String[][] invalidTimes = {
        { "14:30", "Valid", "Time" },
        { "invalid", "Bad", "Time" },
        { "09:00", "Another", "Valid" }
    };

    // Should not throw exception
    assertDoesNotThrow(() -> DataUtils.sortTableByTime(invalidTimes));
  }

  @Test
  @DisplayName("Should preserve row data during sort")
  void testSortPreservesRowData() {
    String[][] table = {
        { "15:00", "Activity1", "Data1" },
        { "10:00", "Activity2", "Data2" },
        { "12:00", "Activity3", "Data3" }
    };

    String[][] sorted = DataUtils.sortTableByTime(table);

    // Check that the associated data moved with the time
    assertEquals("Activity2", sorted[0][1]);
    assertEquals("Data2", sorted[0][2]);
    assertEquals("Activity3", sorted[1][1]);
    assertEquals("Activity1", sorted[2][1]);
  }
}
