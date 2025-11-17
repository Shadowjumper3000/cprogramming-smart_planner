package com.smartplanner.ui.pages;

import com.smartplanner.service.TimeTableService;
import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Time table page for managing weekly schedules.
 */
public class TimeTablePage extends BasePage implements ActionListener {
    private TimeTableService timeTableService;
    private JTable timeTable;
    private DefaultTableModel tableModel;
    private JTextField timeField;
    private JButton addTimeSlotButton, deleteTimeSlotButton, saveButton, refreshButton, switchTableButton;
    private JLabel statusLabel, currentTableLabel;

    public TimeTablePage(String userID) {
        this.userID = userID;
        this.timeTableService = new TimeTableService();
        initializeComponents();
        setupLayout();
        setupFrame();
        loadTimeTableData();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Time Table");
        frame.setSize(1100, 700);

        // Back button
        undoBackButton = new JButton("Back to Main");
        undoBackButton.addActionListener(this);

        // Time input field for adding new time slots
        timeField = new JTextField(8);

        // Action buttons
        addTimeSlotButton = new JButton("Add Time Slot");
        deleteTimeSlotButton = new JButton("Delete Selected Time");
        saveButton = new JButton("Save Changes");
        refreshButton = new JButton("Refresh");
        switchTableButton = new JButton("Switch Table");

        // Status and info labels
        statusLabel = new JLabel("Ready");
        currentTableLabel = new JLabel("Current: " + timeTableService.getCurrentTableFileName());

        // Time table
        String[] columnNames = timeTableService.getTableHeaders();
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0; // Only allow editing of day columns, not time column
            }
        };
        timeTable = new JTable(tableModel);
        timeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        timeTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Time column
        for (int i = 1; i < columnNames.length; i++) {
            timeTable.getColumnModel().getColumn(i).setPreferredWidth(120); // Day columns
        }

        // Add action listeners
        addTimeSlotButton.addActionListener(this);
        deleteTimeSlotButton.addActionListener(this);
        saveButton.addActionListener(this);
        refreshButton.addActionListener(this);
        switchTableButton.addActionListener(this);
    }

    private void setupLayout() {
        frame.setLayout(new BorderLayout());

        // Top panel - Controls
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Time Table Controls"));

        controlPanel.add(new JLabel("Add Time Slot (HH:MM):"));
        controlPanel.add(timeField);
        controlPanel.add(addTimeSlotButton);
        controlPanel.add(deleteTimeSlotButton);
        controlPanel.add(saveButton);
        controlPanel.add(refreshButton);
        controlPanel.add(switchTableButton);

        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(currentTableLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(statusLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.SOUTH);

        // Center panel - Table
        JScrollPane tableScrollPane = new JScrollPane(timeTable);
        tableScrollPane.setPreferredSize(new Dimension(1050, 450));

        // Instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        String instructions = "<html><b>How to use:</b><br>" +
                "• Click on any cell (except Time column) to edit activities<br>" +
                "• Add new time slots using the time field and 'Add Time Slot' button<br>" +
                "• Select a row and click 'Delete Selected Time' to remove a time slot<br>" +
                "• Click 'Save Changes' to save your modifications<br>" +
                "• Use 'Switch Table' to toggle between time_table.csv and time_table2.csv<br>" +
                "• Time format: HH:MM (24-hour format)</html>";
        JLabel instructionsLabel = new JLabel(instructions);
        instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(instructionsPanel, BorderLayout.CENTER);
        bottomPanel.add(undoBackButton, BorderLayout.EAST);

        // Main layout
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void setupFrame() {
        if (frame != null) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null); // Center on screen
        }
    }

    private void loadTimeTableData() {
        tableModel.setRowCount(0); // Clear existing data

        String[][] data = timeTableService.getTimeTableAsTableData();

        for (String[] row : data) {
            tableModel.addRow(row);
        }

        // Update current table label
        currentTableLabel.setText("Current: " + timeTableService.getCurrentTableFileName());
        statusLabel.setText("Loaded " + data.length + " time slots");
    }

    private void saveTableChanges() {
        // Get data from table model
        int rowCount = tableModel.getRowCount();
        int colCount = tableModel.getColumnCount();
        String[][] tableData = new String[rowCount][colCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                Object value = tableModel.getValueAt(i, j);
                tableData[i][j] = (value != null) ? value.toString() : "";
            }
        }

        // Update service with new data
        timeTableService.updateFromTableData(tableData);
        statusLabel.setText("Changes saved successfully");

        // Show confirmation message
        JOptionPane.showMessageDialog(frame,
                "Time table changes saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void addNewTimeSlot() {
        String timeText = timeField.getText().trim();
        if (timeText.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter a time in HH:MM format",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalTime.parse(timeText); // Validate time format
            boolean added = timeTableService.addTimeSlot(timeText);

            if (added) {
                loadTimeTableData();
                timeField.setText("");
                statusLabel.setText("Time slot " + timeText + " added");
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Time slot " + timeText + " already exists",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(frame,
                    "Invalid time format. Please use HH:MM (24-hour format)",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedTimeSlot() {
        int selectedRow = timeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame,
                    "Please select a time slot to delete",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String timeText = (String) tableModel.getValueAt(selectedRow, 0);
        if (timeText == null || timeText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Invalid time slot selected",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete the time slot at " + timeText + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                LocalTime time = LocalTime.parse(timeText);
                timeTableService.deleteEntry(time);
                loadTimeTableData();
                statusLabel.setText("Time slot " + timeText + " deleted");
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(frame,
                        "Error parsing time for deletion",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void switchTimeTable() {
        timeTableService.switchTimeTable();
        loadTimeTableData();
        statusLabel.setText("Switched to " + timeTableService.getCurrentTableFileName());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == undoBackButton) {
            frame.dispose();
            new MainPage(userID);
        } else if (e.getSource() == addTimeSlotButton) {
            addNewTimeSlot();
        } else if (e.getSource() == deleteTimeSlotButton) {
            deleteSelectedTimeSlot();
        } else if (e.getSource() == saveButton) {
            saveTableChanges();
        } else if (e.getSource() == refreshButton) {
            timeTableService.loadTimeTable();
            loadTimeTableData();
            statusLabel.setText("Time table refreshed");
        } else if (e.getSource() == switchTableButton) {
            switchTimeTable();
        }
    }
}