package com.smartplanner.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages application data files and initialization.
 */
public class FileManager {

    private static final String DATA_DIR = "data";
    private static final String LOGIN_INFO_FILE = DATA_DIR + File.separator + "login_info.csv";
    private static final String PLANNER_FILE = DATA_DIR + File.separator + "planner.csv";
    private static final String TIME_TABLE_FILE = DATA_DIR + File.separator + "time_table.csv";
    private static final String TIME_TABLE2_FILE = DATA_DIR + File.separator + "time_table2.csv";

    /**
     * Initializes all required data files for the application.
     */
    public void initializeDataFiles() {
        ensureDataDirectoryExists();
        createLoginInfoFile();
        createPlannerFile();
        createTimeTableFile();
        createTimeTable2File();
    }

    /**
     * Ensures the data directory exists.
     */
    private void ensureDataDirectoryExists() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("Data directory created: " + dataDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create data directory: " + dataDir.getAbsolutePath());
            }
        }
    }

    /**
     * Creates the login_info.csv file if it doesn't exist.
     */
    private void createLoginInfoFile() {
        createFileIfNotExists(LOGIN_INFO_FILE);
    }

    /**
     * Creates the planner.csv file if it doesn't exist.
     */
    private void createPlannerFile() {
        createFileIfNotExists(PLANNER_FILE);
    }

    /**
     * Creates the time_table.csv file with initial data if it doesn't exist.
     */
    private void createTimeTableFile() {
        if (createFileIfNotExists(TIME_TABLE_FILE)) {
            initializeTimeTableWithDefaultData(TIME_TABLE_FILE);
        }
    }

    /**
     * Creates the time_table2.csv file with initial data if it doesn't exist.
     */
    private void createTimeTable2File() {
        if (createFileIfNotExists(TIME_TABLE2_FILE)) {
            initializeTimeTableWithDefaultData(TIME_TABLE2_FILE);
        }
    }

    /**
     * Creates a file if it doesn't exist.
     * 
     * @param filename the name of the file to create
     * @return true if the file was created, false if it already existed
     */
    private boolean createFileIfNotExists(String filename) {
        try {
            File file = new File(filename);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                return true;
            } else {
                System.out.println("File already exists: " + file.getName());
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred creating file: " + filename);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Initializes a time table file with default data.
     * 
     * @param filename the time table file to initialize
     */
    private void initializeTimeTableWithDefaultData(String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            for (int i = 0; i < 6; i++) {
                // null is a hidden last element of the array which is never displayed
                // but allows the user to delete all other elements without causing
                // the timetable model to crash
                fileWriter.write("00:00,,,,,,null,\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred during writing to: " + filename);
            e.printStackTrace();
        }
    }
}