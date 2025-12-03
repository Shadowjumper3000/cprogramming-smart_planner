package com.smartplanner.service;

import com.smartplanner.model.TimeTableEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TimeTableService Tests")
class TimeTableServiceTest {

  @TempDir
  Path tempDir;

  private TimeTableService timeTableService;

  @BeforeEach
  void setUp() throws IOException {
    // Create temporary data directory and timetable files
    File dataDir = tempDir.resolve("data").toFile();
    dataDir.mkdirs();

    File timeTable1 = new File(dataDir, "time_table.csv");
    File timeTable2 = new File(dataDir, "time_table2.csv");
    timeTable1.createNewFile();
    timeTable2.createNewFile();

    // Set system property to use temp directory
    System.setProperty("user.dir", tempDir.toString());

    timeTableService = new TimeTableService();
  }

  @Test
  @DisplayName("Should initialize with entries")
  void testInitialization() {
    List<TimeTableEntry> entries = timeTableService.getAllEntries();
    assertNotNull(entries);
  }

  @Test
  @DisplayName("Should add entry successfully")
  void testAddEntry() {
    TimeTableEntry entry = new TimeTableEntry();
    entry.setTime(LocalTime.of(9, 0));
    entry.setMonday("Math Class");
    entry.setTuesday("Physics Lab");

    timeTableService.addEntry(entry);

    List<TimeTableEntry> entries = timeTableService.getAllEntries();
    assertTrue(entries.size() > 0);
  }

  @Test
  @DisplayName("Should update entry by time and day")
  void testUpdateEntry() {
    LocalTime time = LocalTime.of(10, 0);
    timeTableService.updateEntry(time, 0, "Updated Activity");

    TimeTableEntry entry = timeTableService.findEntryByTime(time);
    assertNotNull(entry);
    assertEquals("Updated Activity", entry.getMonday());
  }

  @Test
  @DisplayName("Should delete entry by time")
  void testDeleteEntry() {
    LocalTime time = LocalTime.of(11, 0);
    TimeTableEntry entry = new TimeTableEntry();
    entry.setTime(time);
    entry.setMonday("To Delete");

    timeTableService.addEntry(entry);
    assertNotNull(timeTableService.findEntryByTime(time));

    timeTableService.deleteEntry(time);
    assertNull(timeTableService.findEntryByTime(time));
  }

  @Test
  @DisplayName("Should find entry by time")
  void testFindEntryByTime() {
    LocalTime time = LocalTime.of(14, 30);
    TimeTableEntry entry = new TimeTableEntry();
    entry.setTime(time);
    entry.setMonday("Lunch Break");

    timeTableService.addEntry(entry);

    TimeTableEntry found = timeTableService.findEntryByTime(time);
    assertNotNull(found);
    assertEquals("Lunch Break", found.getMonday());
  }

  @Test
  @DisplayName("Should get activities for specific day")
  void testGetActivitiesForDay() {
    LocalTime time = LocalTime.of(9, 0);
    TimeTableEntry entry = new TimeTableEntry();
    entry.setTime(time);
    entry.setMonday("Morning Meeting");
    entry.setTuesday("Team Standup");

    timeTableService.addEntry(entry);

    Map<LocalTime, String> mondayActivities = timeTableService.getActivitiesForDay(0);
    Map<LocalTime, String> tuesdayActivities = timeTableService.getActivitiesForDay(1);

    assertTrue(mondayActivities.containsKey(time));
    assertEquals("Morning Meeting", mondayActivities.get(time));
    assertEquals("Team Standup", tuesdayActivities.get(time));
  }

  @Test
  @DisplayName("Should convert timetable to table data")
  void testGetTimeTableAsTableData() {
    TimeTableEntry entry = new TimeTableEntry();
    entry.setTime(LocalTime.of(10, 0));
    entry.setMonday("Class");
    entry.setTuesday("Lab");

    timeTableService.addEntry(entry);

    String[][] tableData = timeTableService.getTimeTableAsTableData();

    assertNotNull(tableData);
    assertTrue(tableData.length > 0);
  }

  @Test
  @DisplayName("Should get table headers")
  void testGetTableHeaders() {
    String[] headers = timeTableService.getTableHeaders();

    assertEquals(8, headers.length);
    assertEquals("Time", headers[0]);
    assertEquals("Monday", headers[1]);
    assertEquals("Tuesday", headers[2]);
    assertEquals("Wednesday", headers[3]);
    assertEquals("Thursday", headers[4]);
    assertEquals("Friday", headers[5]);
    assertEquals("Saturday", headers[6]);
    assertEquals("Sunday", headers[7]);
  }

  @Test
  @DisplayName("Should switch between timetables")
  void testSwitchTimeTable() {
    String initialTable = timeTableService.getCurrentTableFileName();

    timeTableService.switchTimeTable();
    String switchedTable = timeTableService.getCurrentTableFileName();

    assertNotEquals(initialTable, switchedTable);

    timeTableService.switchTimeTable();
    String backToInitial = timeTableService.getCurrentTableFileName();

    assertEquals(initialTable, backToInitial);
  }

  @Test
  @DisplayName("Should update from table data array")
  void testUpdateFromTableData() {
    String[][] tableData = new String[][] {
        { "09:00", "Math", "Physics", "Chemistry", "Biology", "English", "", "" },
        { "10:00", "Break", "Break", "Break", "Break", "Break", "", "" }
    };

    timeTableService.updateFromTableData(tableData);

    List<TimeTableEntry> entries = timeTableService.getAllEntries();
    assertTrue(entries.size() >= 2);
  }

  @Test
  @DisplayName("Should add new time slot")
  void testAddTimeSlot() {
    boolean added = timeTableService.addTimeSlot("15:30");

    assertTrue(added);
    assertNotNull(timeTableService.findEntryByTime(LocalTime.of(15, 30)));
  }

  @Test
  @DisplayName("Should not add duplicate time slot")
  void testAddDuplicateTimeSlot() {
    timeTableService.addTimeSlot("16:00");
    boolean addedAgain = timeTableService.addTimeSlot("16:00");

    assertFalse(addedAgain);
  }

  @Test
  @DisplayName("Should handle invalid time format")
  void testAddInvalidTimeSlot() {
    boolean added = timeTableService.addTimeSlot("invalid");

    assertFalse(added);
  }

  @Test
  @DisplayName("Should replace existing entry when adding with same time")
  void testReplaceExistingEntry() {
    LocalTime time = LocalTime.of(13, 0);

    TimeTableEntry entry1 = new TimeTableEntry();
    entry1.setTime(time);
    entry1.setMonday("Original");
    timeTableService.addEntry(entry1);

    TimeTableEntry entry2 = new TimeTableEntry();
    entry2.setTime(time);
    entry2.setMonday("Updated");
    timeTableService.addEntry(entry2);

    TimeTableEntry found = timeTableService.findEntryByTime(time);
    assertEquals("Updated", found.getMonday());
  }
}
