package com.smartplanner.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TimeTableEntry Model Tests")
class TimeTableEntryTest {

  private TimeTableEntry entry;

  @BeforeEach
  void setUp() {
    entry = new TimeTableEntry();
  }

  @Test
  @DisplayName("Should create entry with default empty strings")
  void testDefaultConstructor() {
    assertNotNull(entry);
    assertEquals("", entry.getMonday());
    assertEquals("", entry.getTuesday());
    assertEquals("", entry.getWednesday());
    assertEquals("", entry.getThursday());
    assertEquals("", entry.getFriday());
    assertEquals("", entry.getSaturday());
    assertEquals("", entry.getSunday());
  }

  @Test
  @DisplayName("Should create entry with all parameters")
  void testParameterizedConstructor() {
    LocalTime time = LocalTime.of(9, 0);
    TimeTableEntry entry = new TimeTableEntry(time, "Math", "Physics",
        "Chemistry", "Biology",
        "English", "History", "Rest");

    assertEquals(time, entry.getTime());
    assertEquals("Math", entry.getMonday());
    assertEquals("Physics", entry.getTuesday());
    assertEquals("Chemistry", entry.getWednesday());
    assertEquals("Biology", entry.getThursday());
    assertEquals("English", entry.getFriday());
    assertEquals("History", entry.getSaturday());
    assertEquals("Rest", entry.getSunday());
  }

  @Test
  @DisplayName("Should set and get all day activities")
  void testGettersAndSetters() {
    LocalTime time = LocalTime.of(10, 30);

    entry.setTime(time);
    entry.setMonday("Meeting");
    entry.setTuesday("Workshop");
    entry.setWednesday("Presentation");
    entry.setThursday("Review");
    entry.setFriday("Planning");
    entry.setSaturday("Gym");
    entry.setSunday("Free");

    assertEquals(time, entry.getTime());
    assertEquals("Meeting", entry.getMonday());
    assertEquals("Workshop", entry.getTuesday());
    assertEquals("Presentation", entry.getWednesday());
    assertEquals("Review", entry.getThursday());
    assertEquals("Planning", entry.getFriday());
    assertEquals("Gym", entry.getSaturday());
    assertEquals("Free", entry.getSunday());
  }

  @Test
  @DisplayName("Should convert entry to CSV string correctly")
  void testToCsvString() {
    entry.setTime(LocalTime.of(14, 0));
    entry.setMonday("Lunch");
    entry.setTuesday("Lunch");
    entry.setWednesday("Lunch");
    entry.setThursday("Lunch");
    entry.setFriday("Lunch");
    entry.setSaturday("");
    entry.setSunday("");

    String csv = entry.toCsvString();
    assertEquals("14:00,Lunch,Lunch,Lunch,Lunch,Lunch,,", csv);
  }

  @Test
  @DisplayName("Should create entry from CSV string")
  void testFromCsvString() {
    String csvLine = "09:00,Math,Physics,Chemistry,Biology,English,History,Rest";
    TimeTableEntry entry = TimeTableEntry.fromCsvString(csvLine);

    assertNotNull(entry);
    assertEquals(LocalTime.of(9, 0), entry.getTime());
    assertEquals("Math", entry.getMonday());
    assertEquals("Physics", entry.getTuesday());
    assertEquals("Chemistry", entry.getWednesday());
    assertEquals("Biology", entry.getThursday());
    assertEquals("English", entry.getFriday());
    assertEquals("History", entry.getSaturday());
    assertEquals("Rest", entry.getSunday());
  }

  @Test
  @DisplayName("Should create entry from CSV with empty fields")
  void testFromCsvStringWithEmptyFields() {
    String csvLine = "10:00,Meeting,,,Workshop,,,";
    TimeTableEntry entry = TimeTableEntry.fromCsvString(csvLine);

    assertNotNull(entry);
    assertEquals(LocalTime.of(10, 0), entry.getTime());
    assertEquals("Meeting", entry.getMonday());
    assertEquals("", entry.getTuesday());
    assertEquals("", entry.getWednesday());
    assertEquals("Workshop", entry.getThursday());
  }

  @Test
  @DisplayName("Should get activity by day index")
  void testGetActivityByDayIndex() {
    entry.setMonday("Mon");
    entry.setTuesday("Tue");
    entry.setWednesday("Wed");
    entry.setThursday("Thu");
    entry.setFriday("Fri");
    entry.setSaturday("Sat");
    entry.setSunday("Sun");

    assertEquals("Mon", entry.getActivityByDayIndex(0));
    assertEquals("Tue", entry.getActivityByDayIndex(1));
    assertEquals("Wed", entry.getActivityByDayIndex(2));
    assertEquals("Thu", entry.getActivityByDayIndex(3));
    assertEquals("Fri", entry.getActivityByDayIndex(4));
    assertEquals("Sat", entry.getActivityByDayIndex(5));
    assertEquals("Sun", entry.getActivityByDayIndex(6));
    assertEquals("", entry.getActivityByDayIndex(7)); // Invalid index
  }

  @Test
  @DisplayName("Should set activity by day index")
  void testSetActivityByDayIndex() {
    entry.setActivityByDayIndex(0, "Monday Activity");
    entry.setActivityByDayIndex(1, "Tuesday Activity");
    entry.setActivityByDayIndex(2, "Wednesday Activity");
    entry.setActivityByDayIndex(3, "Thursday Activity");
    entry.setActivityByDayIndex(4, "Friday Activity");
    entry.setActivityByDayIndex(5, "Saturday Activity");
    entry.setActivityByDayIndex(6, "Sunday Activity");

    assertEquals("Monday Activity", entry.getMonday());
    assertEquals("Tuesday Activity", entry.getTuesday());
    assertEquals("Wednesday Activity", entry.getWednesday());
    assertEquals("Thursday Activity", entry.getThursday());
    assertEquals("Friday Activity", entry.getFriday());
    assertEquals("Saturday Activity", entry.getSaturday());
    assertEquals("Sunday Activity", entry.getSunday());
  }

  @Test
  @DisplayName("Should handle invalid day index gracefully")
  void testInvalidDayIndex() {
    entry.setActivityByDayIndex(10, "Invalid"); // Should not throw exception
    assertEquals("", entry.getActivityByDayIndex(10));
  }

  @Test
  @DisplayName("Should return null for invalid CSV string")
  void testFromCsvStringInvalid() {
    String csvLine = "09:00,Math";
    TimeTableEntry entry = TimeTableEntry.fromCsvString(csvLine);

    assertNull(entry);
  }
}
