package com.smartplanner.utils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for handling CSV data operations with JTable components.
 */
public class DataUtils {

    /**
     * Downloads data from a CSV file into a 2D array.
     * 
     * @param file the path to the CSV file
     * @return 2D String array containing the CSV data
     */
    public static String[][] loadCsvData(String file) {
        List<List<String>> list = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line == null) {
                return new String[0][0];
            }
            
            String[] headers = line.split(",");
            for (String header : headers) {
                List<String> subList = new ArrayList<>();
                subList.add(header);
                list.add(subList);
            }
            
            while ((line = br.readLine()) != null) {
                String[] elems = line.split(",");
                for (int i = 0; i < elems.length; i++) {
                    if (i < list.size()) {
                        list.get(i).add(elems[i]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + file);
            e.printStackTrace();
            return new String[0][0];
        }
        
        if (list.isEmpty()) {
            return new String[0][0];
        }
        
        int rows = list.size();
        int cols = list.get(0).size();
        
        System.out.println("Loaded data: " + list + " --1");
        System.out.println("Rows: " + rows);
        System.out.println("Columns: " + cols);
        
        String[][] array2D = new String[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                array2D[row][col] = list.get(row).get(col);
            }
        }
        
        System.out.println("Array2D: " + Arrays.deepToString(array2D) + " --2");
        
        // Rearrange the array into a compatible format (transpose)
        String[][] improvedArray = new String[array2D[0].length][array2D.length];
        
        for (int i = 0; i < improvedArray.length; i++) {
            for (int j = 0; j < improvedArray[i].length; j++) {
                improvedArray[i][j] = array2D[j][i];
            }
        }
        
        System.out.println("Improved array: " + Arrays.deepToString(improvedArray) + " --improved");
        return improvedArray;
    }

    /**
     * Extracts data from a JTable into a 2D String array.
     * 
     * @param table the JTable to extract data from
     * @return 2D String array containing the table data
     */
    public static String[][] getTableData(JTable table) {
        TableModel model = table.getModel();
        int rowCount = model.getRowCount();
        int colCount = model.getColumnCount();
        
        String[][] tableData = new String[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                Object value = model.getValueAt(i, j);
                tableData[i][j] = (value != null) ? value.toString() : "";
            }
        }
        return tableData;
    }

    /**
     * Saves table data to a CSV file.
     * 
     * @param table the JTable containing data to save
     * @param filePath the path where to save the CSV file
     */
    public static void saveTableData(JTable table, String filePath) {
        String[][] data = getTableData(table);
        saveCsvData(data, filePath);
    }

    /**
     * Saves a 2D array to a CSV file.
     * 
     * @param data the 2D array to save
     * @param filePath the path where to save the CSV file
     */
    public static void saveCsvData(String[][] data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : data) {
                for (String cell : row) {
                    writer.write(cell + ",");
                }
                writer.write("\n");
            }
        } catch (IOException ex) {
            System.err.println("Error saving CSV file: " + filePath);
            ex.printStackTrace();
        }
    }

    /**
     * Sorts a time table by time values using bubble sort.
     * 
     * @param table the table to sort (assumes first column contains time in HH:mm format)
     * @return the sorted table
     */
    public static String[][] sortTableByTime(String[][] table) {
        int n = table.length;
        boolean swapped;
        
        do {
            swapped = false;
            for (int j = 0; j < n - 1; j++) {
                try {
                    int time1 = Integer.parseInt(table[j][0].replace(":", ""));
                    int time2 = Integer.parseInt(table[j + 1][0].replace(":", ""));
                    
                    if (time1 > time2) {
                        // Swap entire rows
                        String[] temp = table[j];
                        table[j] = table[j + 1];
                        table[j + 1] = temp;
                        swapped = true;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing time format in row " + j);
                }
            }
            n--;
        } while (swapped && n > 0);
        
        System.out.println("Sorted table: " + Arrays.deepToString(table));
        return table;
    }
}