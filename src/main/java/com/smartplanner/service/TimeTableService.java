package com.smartplanner.service;

import com.smartplanner.model.TimeTableEntry;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Service class for managing timetable data.
 */
public class TimeTableService {
  private static final String TIME_TABLE_FILE = "data/time_table.csv";
  private static final String TIME_TABLE2_FILE = "data/time_table2.csv";
  private final List<TimeTableEntry> entries;
  private String currentTableFile;

  public TimeTableService() {
    this.entries = new ArrayList<>();
    this.currentTableFile = TIME_TABLE_FILE;
    loadTimeTable();
  }

  /**
   * Loads timetable entries from the current CSV file.
   */
  public void loadTimeTable() {
    entries.clear();
    File file = new File(currentTableFile);

    if (!file.exists()) {
      initializeEmptyTimeTable();
      return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          TimeTableEntry entry = TimeTableEntry.fromCsvString(line);
          if (entry != null && !isNullEntry(entry)) {
            entries.add(entry);
          }
        }
      }
    } catch (IOException e) {
      System.err.println("Error loading timetable from file: " + e.getMessage());
    }

    // Sort entries by time
    entries.sort((e1, e2) -> {
      if (e1.getTime() == null && e2.getTime() == null)
        return 0;
      if (e1.getTime() == null)
        return 1;
      if (e2.getTime() == null)
        return -1;
      return e1.getTime().compareTo(e2.getTime());
    });
  }

  /**
   * Saves all timetable entries to the current CSV file.
   */
  public void saveTimeTable() {
    try (FileWriter writer = new FileWriter(currentTableFile)) {
      for (TimeTableEntry entry : entries) {
        writer.write(entry.toCsvString() + "\n");
      }
      // Add the hidden null entry at the end as per original design
      writer.write("00:00,,,,,,null,\n");
    } catch (IOException e) {
      System.err.println("Error saving timetable to file: " + e.getMessage());
    }
  }

  /**
   * Adds a new timetable entry.
   */
  public void addEntry(TimeTableEntry entry) {
    if (entry != null && entry.getTime() != null) {
      // Check if entry with this time already exists
      entries.removeIf(existing -> existing.getTime().equals(entry.getTime()));
      entries.add(entry);
      saveTimeTable();
    }
  }

  /**
   * Updates an existing timetable entry.
   */
  public void updateEntry(LocalTime time, int dayIndex, String activity) {
    TimeTableEntry entry = findEntryByTime(time);
    if (entry != null) {
      entry.setActivityByDayIndex(dayIndex, activity);
    } else {
      // Create new entry if it doesn't exist
      entry = new TimeTableEntry();
      entry.setTime(time);
      entry.setActivityByDayIndex(dayIndex, activity);
      entries.add(entry);
    }
    saveTimeTable();
  }

  /**
   * Deletes a timetable entry by time.
   */
  public void deleteEntry(LocalTime time) {
    entries.removeIf(entry -> entry.getTime().equals(time));
    saveTimeTable();
  }

  /**
   * Gets all timetable entries.
   */
  public List<TimeTableEntry> getAllEntries() {
    return new ArrayList<>(entries);
  }

  /**
   * Finds an entry by time.
   */
  public TimeTableEntry findEntryByTime(LocalTime time) {
    return entries.stream()
        .filter(entry -> entry.getTime().equals(time))
        .findFirst()
        .orElse(null);
  }

  /**
   * Gets activities for a specific day (0 = Monday, 6 = Sunday).
   */
  public Map<LocalTime, String> getActivitiesForDay(int dayIndex) {
    Map<LocalTime, String> dayActivities = new HashMap<>();
    for (TimeTableEntry entry : entries) {
      String activity = entry.getActivityByDayIndex(dayIndex);
      if (activity != null && !activity.trim().isEmpty()) {
        dayActivities.put(entry.getTime(), activity);
      }
    }
    return dayActivities;
  }

  /**
   * Converts timetable to 2D array for table display.
   */
  public String[][] getTimeTableAsTableData() {
    if (entries.isEmpty()) {
      initializeEmptyTimeTable();
    }

    String[][] data = new String[entries.size()][8];

    for (int i = 0; i < entries.size(); i++) {
      TimeTableEntry entry = entries.get(i);
      data[i][0] = entry.getTime() != null ? entry.getTime().toString() : "";
      data[i][1] = entry.getMonday() != null ? entry.getMonday() : "";
      data[i][2] = entry.getTuesday() != null ? entry.getTuesday() : "";
      data[i][3] = entry.getWednesday() != null ? entry.getWednesday() : "";
      data[i][4] = entry.getThursday() != null ? entry.getThursday() : "";
      data[i][5] = entry.getFriday() != null ? entry.getFriday() : "";
      data[i][6] = entry.getSaturday() != null ? entry.getSaturday() : "";
      data[i][7] = entry.getSunday() != null ? entry.getSunday() : "";
    }

    return data;
  }

  /**
   * Gets table column headers for timetable display.
   */
  public String[] getTableHeaders() {
    return new String[] { "Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
  }

  /**
   * Switches between time_table.csv and time_table2.csv.
   */
  public void switchTimeTable() {
    if (currentTableFile.equals(TIME_TABLE_FILE)) {
      currentTableFile = TIME_TABLE2_FILE;
    } else {
      currentTableFile = TIME_TABLE_FILE;
    }
    loadTimeTable();
  }

  /**
   * Gets the current timetable file name.
   */
  public String getCurrentTableFileName() {
    return currentTableFile.substring(currentTableFile.lastIndexOf('/') + 1);
  }

  /**
   * Updates table data from a 2D array (for when user edits the table directly).
   */
  public void updateFromTableData(String[][] tableData) {
    entries.clear();

    for (String[] row : tableData) {
      if (row.length >= 8 && !row[0].trim().isEmpty()) {
        try {
          TimeTableEntry entry = new TimeTableEntry();
          entry.setTime(LocalTime.parse(row[0].trim()));
          entry.setMonday(row[1] != null ? row[1] : "");
          entry.setTuesday(row[2] != null ? row[2] : "");
          entry.setWednesday(row[3] != null ? row[3] : "");
          entry.setThursday(row[4] != null ? row[4] : "");
          entry.setFriday(row[5] != null ? row[5] : "");
          entry.setSaturday(row[6] != null ? row[6] : "");
          entry.setSunday(row[7] != null ? row[7] : "");

          if (!isNullEntry(entry)) {
            entries.add(entry);
          }
        } catch (DateTimeParseException e) {
          System.err.println("Invalid time format in row: " + row[0]);
        }
      }
    }

    saveTimeTable();
  }

  /**
   * Initializes an empty timetable with some default time slots.
   */
  private void initializeEmptyTimeTable() {
    entries.clear();

    // Add some default time slots
    String[] defaultTimes = { "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
        "17:00" };

    for (String timeStr : defaultTimes) {
      TimeTableEntry entry = new TimeTableEntry();
      entry.setTime(LocalTime.parse(timeStr));
      entries.add(entry);
    }
  }

  /**
   * Checks if an entry is the special "null" entry that should be filtered out.
   */
  private boolean isNullEntry(TimeTableEntry entry) {
    return entry.getMonday() != null && entry.getMonday().equals("null");
  }

  /**
   * Adds a new time slot to the timetable.
   */
  public boolean addTimeSlot(String timeStr) {
    try {
      LocalTime time = LocalTime.parse(timeStr);

      // Check if time slot already exists
      boolean exists = entries.stream()
          .anyMatch(entry -> entry.getTime().equals(time));

      if (!exists) {
        TimeTableEntry newEntry = new TimeTableEntry();
        newEntry.setTime(time);
        entries.add(newEntry);
        saveTimeTable();
        return true;
      }
      return false;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}