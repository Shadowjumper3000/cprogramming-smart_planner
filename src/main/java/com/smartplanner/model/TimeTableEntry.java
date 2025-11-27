package com.smartplanner.model;

import java.time.LocalTime;

/**
 * Represents a timetable entry for a specific day and time.
 */
public class TimeTableEntry {
  private LocalTime time;
  private String monday;
  private String tuesday;
  private String wednesday;
  private String thursday;
  private String friday;
  private String saturday;
  private String sunday;

  public TimeTableEntry() {
    this.monday = "";
    this.tuesday = "";
    this.wednesday = "";
    this.thursday = "";
    this.friday = "";
    this.saturday = "";
    this.sunday = "";
  }

  public TimeTableEntry(LocalTime time, String monday, String tuesday, String wednesday,
      String thursday, String friday, String saturday, String sunday) {
    this.time = time;
    this.monday = monday;
    this.tuesday = tuesday;
    this.wednesday = wednesday;
    this.thursday = thursday;
    this.friday = friday;
    this.saturday = saturday;
    this.sunday = sunday;
  }

  // Getters and setters
  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  public String getMonday() {
    return monday;
  }

  public void setMonday(String monday) {
    this.monday = monday;
  }

  public String getTuesday() {
    return tuesday;
  }

  public void setTuesday(String tuesday) {
    this.tuesday = tuesday;
  }

  public String getWednesday() {
    return wednesday;
  }

  public void setWednesday(String wednesday) {
    this.wednesday = wednesday;
  }

  public String getThursday() {
    return thursday;
  }

  public void setThursday(String thursday) {
    this.thursday = thursday;
  }

  public String getFriday() {
    return friday;
  }

  public void setFriday(String friday) {
    this.friday = friday;
  }

  public String getSaturday() {
    return saturday;
  }

  public void setSaturday(String saturday) {
    this.saturday = saturday;
  }

  public String getSunday() {
    return sunday;
  }

  public void setSunday(String sunday) {
    this.sunday = sunday;
  }

  /**
   * Converts the timetable entry to CSV format for file storage.
   */
  public String toCsvString() {
    return String.join(",",
        time != null ? time.toString() : "00:00",
        monday != null ? monday : "",
        tuesday != null ? tuesday : "",
        wednesday != null ? wednesday : "",
        thursday != null ? thursday : "",
        friday != null ? friday : "",
        saturday != null ? saturday : "",
        sunday != null ? sunday : "");
  }

  /**
   * Creates a TimeTableEntry from CSV string.
   */
  public static TimeTableEntry fromCsvString(String csvLine) {
    String[] parts = csvLine.split(",", -1); // -1 to keep empty strings
    if (parts.length < 8) {
      return null;
    }

    TimeTableEntry entry = new TimeTableEntry();

    if (!parts[0].trim().isEmpty()) {
      entry.setTime(LocalTime.parse(parts[0].trim()));
    }

    entry.setMonday(parts[1].trim());
    entry.setTuesday(parts[2].trim());
    entry.setWednesday(parts[3].trim());
    entry.setThursday(parts[4].trim());
    entry.setFriday(parts[5].trim());
    entry.setSaturday(parts[6].trim());
    entry.setSunday(parts[7].trim());

    return entry;
  }

  /**
   * Gets activity for a specific day by index (0 = Monday, 6 = Sunday).
   */
  public String getActivityByDayIndex(int dayIndex) {
    switch (dayIndex) {
      case 0:
        return monday;
      case 1:
        return tuesday;
      case 2:
        return wednesday;
      case 3:
        return thursday;
      case 4:
        return friday;
      case 5:
        return saturday;
      case 6:
        return sunday;
      default:
        return "";
    }
  }

  /**
   * Sets activity for a specific day by index (0 = Monday, 6 = Sunday).
   */
  public void setActivityByDayIndex(int dayIndex, String activity) {
    switch (dayIndex) {
      case 0:
        monday = activity;
        break;
      case 1:
        tuesday = activity;
        break;
      case 2:
        wednesday = activity;
        break;
      case 3:
        thursday = activity;
        break;
      case 4:
        friday = activity;
        break;
      case 5:
        saturday = activity;
        break;
      case 6:
        sunday = activity;
        break;
    }
  }

  @Override
  public String toString() {
    return "TimeTableEntry{" +
        "time=" + time +
        ", monday='" + monday + '\'' +
        ", tuesday='" + tuesday + '\'' +
        ", wednesday='" + wednesday + '\'' +
        ", thursday='" + thursday + '\'' +
        ", friday='" + friday + '\'' +
        ", saturday='" + saturday + '\'' +
        ", sunday='" + sunday + '\'' +
        '}';
  }
}