package com.smartplanner.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Reusable table component that allows multiple JTables to be in one window.
 */
public class TablePanel extends JPanel {
    private final JTable table;

    /**
     * Creates a new TablePanel with the specified headers and data.
     * 
     * @param headers the column headers for the table
     * @param contents the data content for the table
     */
    public TablePanel(String[] headers, String[][] contents) {
        table = new JTable(contents, headers);
        table.setPreferredScrollableViewportSize(new Dimension(450, 100));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    /**
     * Returns the underlying JTable component.
     * 
     * @return the JTable instance
     */
    public JTable getTable() {
        return table;
    }
}