package com.smartplanner.ui.pages;

import com.smartplanner.model.Task;
import com.smartplanner.service.PlannerService;
import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Planner page for managing tasks and schedules.
 */
public class PlannerPage extends BasePage implements ActionListener {
    private PlannerService plannerService;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JTextField titleField, descriptionField, dueDateField, dueTimeField, categoryField;
    private JComboBox<String> priorityComboBox, filterComboBox;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, markCompleteButton, searchButton, refreshButton, reportButton, emailSettingsButton;
    private JCheckBox showCompletedCheckbox;
    private JCheckBox emailReminderCheckbox;
    private JComboBox<Integer> reminderMinutesComboBox;

    public PlannerPage(String userID) {
        this.userID = userID;
        this.plannerService = new PlannerService();
        initializeComponents();
        setupLayout();
        setupFrame();
        loadTasksIntoTable();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Task Planner");
        frame.setSize(1000, 600);

        // Back button
        undoBackButton = new JButton("Back to Main");
        undoBackButton.addActionListener(this);

        // Task form components
        titleField = new JTextField(20);
        descriptionField = new JTextField(20);
        dueDateField = new JTextField(10); // Format: YYYY-MM-DD
        dueTimeField = new JTextField(8); // Format: HH:MM
        categoryField = new JTextField(15);

        String[] priorities = { "High", "Medium", "Low" };
        priorityComboBox = new JComboBox<>(priorities);
        priorityComboBox.setSelectedItem("Medium");

        // Email reminder components
        emailReminderCheckbox = new JCheckBox("Enable Email Reminder");
        Integer[] reminderOptions = { 5, 10, 15, 30, 60 };
        reminderMinutesComboBox = new JComboBox<>(reminderOptions);
        reminderMinutesComboBox.setSelectedItem(30);

        // Action buttons
        addButton = new JButton("Add Task");
        editButton = new JButton("Edit Task");
        deleteButton = new JButton("Delete Task");
        markCompleteButton = new JButton("Toggle Complete");
        refreshButton = new JButton("Refresh");
        reportButton = new JButton("Generate Report");
        emailSettingsButton = new JButton("Email Settings");

        // Search and filter components
        searchField = new JTextField(15);
        searchButton = new JButton("Search");

        String[] filters = { "All", "High Priority", "Medium Priority", "Low Priority", "Completed", "Pending",
                "Due Today", "Overdue" };
        filterComboBox = new JComboBox<>(filters);

        showCompletedCheckbox = new JCheckBox("Show Completed", true);

        // Task table
        String[] columnNames = { "Title", "Description", "Due Date", "Due Time", "Priority", "Completed", "Category" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only, editing through form
            }
        };
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add table selection listener for auto-loading selected task into form
        taskTable.getSelectionModel().addListSelectionListener(e -> {
            // Only auto-load if form is empty to avoid overwriting user input
            if (!e.getValueIsAdjusting() && isFormEmpty()) {
                loadSelectedTaskIntoForm();
            }
        });

        // Add action listeners
        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        markCompleteButton.addActionListener(this);
        searchButton.addActionListener(this);
        refreshButton.addActionListener(this);
        reportButton.addActionListener(e -> new ReportPage(userID));
        emailSettingsButton.addActionListener(this);
        filterComboBox.addActionListener(this);
        showCompletedCheckbox.addActionListener(this);
    }

    private void setupLayout() {
        frame.setLayout(new BorderLayout());

        // Top panel - Form for adding/editing tasks
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Task Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // First row
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 3;
        formPanel.add(categoryField, gbc);

        // Second row
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 3;
        formPanel.add(priorityComboBox, gbc);

        // Third row
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Due Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        formPanel.add(dueDateField, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Due Time (HH:MM):"), gbc);
        gbc.gridx = 3;
        formPanel.add(dueTimeField, gbc);

        // Fourth row - Email reminder (both on same row in grid)
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(emailReminderCheckbox, gbc);
        gbc.gridx = 1;
        formPanel.add(reminderMinutesComboBox, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("minutes before"), gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(markCompleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(emailSettingsButton);

        // Search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(filterComboBox);
        searchPanel.add(showCompletedCheckbox);

        // Center panel - Table
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        tableScrollPane.setPreferredSize(new Dimension(950, 300));

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(undoBackButton, BorderLayout.EAST);

        // Main layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

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

    private void loadTasksIntoTable() {
        tableModel.setRowCount(0); // Clear existing data

        List<Task> tasks = getCurrentFilteredTasks();

        for (Task task : tasks) {
            Object[] row = {
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate() != null ? task.getDueDate().toString() : "",
                    task.getDueTime() != null ? task.getDueTime().toString() : "",
                    task.getPriority(),
                    task.isCompleted() ? "Yes" : "No",
                    task.getCategory()
            };
            tableModel.addRow(row);
        }
    }

    private List<Task> getCurrentFilteredTasks() {
        String filter = (String) filterComboBox.getSelectedItem();
        List<Task> tasks;

        switch (filter) {
            case "High Priority":
                tasks = plannerService.getTasksByPriority("High");
                break;
            case "Medium Priority":
                tasks = plannerService.getTasksByPriority("Medium");
                break;
            case "Low Priority":
                tasks = plannerService.getTasksByPriority("Low");
                break;
            case "Completed":
                tasks = plannerService.getTasksByCompletion(true);
                break;
            case "Pending":
                tasks = plannerService.getTasksByCompletion(false);
                break;
            case "Due Today":
                tasks = plannerService.getTasksDueToday();
                break;
            case "Overdue":
                tasks = plannerService.getOverdueTasks();
                break;
            default:
                tasks = plannerService.getAllTasks();
                break;
        }

        // Apply completed filter
        if (!showCompletedCheckbox.isSelected()) {
            tasks = tasks.stream()
                    .filter(task -> !task.isCompleted())
                    .collect(java.util.stream.Collectors.toList());
        }

        return tasks;
    }

    private void clearForm() {
        titleField.setText("");
        descriptionField.setText("");
        dueDateField.setText("");
        dueTimeField.setText("");
        categoryField.setText("");
        priorityComboBox.setSelectedItem("Medium");
        emailReminderCheckbox.setSelected(false);
        reminderMinutesComboBox.setSelectedItem(30);
    }

    private boolean isFormEmpty() {
        return titleField.getText().trim().isEmpty() &&
                descriptionField.getText().trim().isEmpty() &&
                dueDateField.getText().trim().isEmpty() &&
                dueTimeField.getText().trim().isEmpty() &&
                categoryField.getText().trim().isEmpty() &&
                priorityComboBox.getSelectedItem().equals("Medium");
    }

    private void loadSelectedTaskIntoForm() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            titleField.setText((String) tableModel.getValueAt(selectedRow, 0));
            descriptionField.setText((String) tableModel.getValueAt(selectedRow, 1));
            dueDateField.setText((String) tableModel.getValueAt(selectedRow, 2));
            dueTimeField.setText((String) tableModel.getValueAt(selectedRow, 3));
            priorityComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
            categoryField.setText((String) tableModel.getValueAt(selectedRow, 6));
            
            // Load email reminder settings from the actual task object
            List<Task> filteredTasks = getCurrentFilteredTasks();
            if (selectedRow < filteredTasks.size()) {
                Task task = filteredTasks.get(selectedRow);
                emailReminderCheckbox.setSelected(task.isEmailReminderEnabled());
                reminderMinutesComboBox.setSelectedItem(task.getReminderMinutesBefore());
            }
        }
    }

    private Task createTaskFromForm() {
        Task task = new Task();
        task.setTitle(titleField.getText().trim());
        task.setDescription(descriptionField.getText().trim());
        task.setPriority((String) priorityComboBox.getSelectedItem());
        task.setCategory(categoryField.getText().trim());

        // Parse due date
        String dueDateText = dueDateField.getText().trim();
        if (!dueDateText.isEmpty()) {
            try {
                task.setDueDate(LocalDate.parse(dueDateText));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Use YYYY-MM-DD", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        // Parse due time
        String dueTimeText = dueTimeField.getText().trim();
        if (!dueTimeText.isEmpty()) {
            try {
                task.setDueTime(LocalTime.parse(dueTimeText));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(frame, "Invalid time format. Use HH:MM", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        // Email reminder settings
        task.setEmailReminderEnabled(emailReminderCheckbox.isSelected());
        task.setReminderMinutesBefore((Integer) reminderMinutesComboBox.getSelectedItem());

        return task;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == undoBackButton) {
            frame.dispose();
            new MainPage(userID);
        } else if (e.getSource() == addButton) {
            Task task = createTaskFromForm();
            if (task != null && !task.getTitle().isEmpty()) {
                plannerService.addTask(task);
                clearForm();
                loadTasksIntoTable();
                JOptionPane.showMessageDialog(frame, "Task added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a task title.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == editButton) {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                // First, load the selected task into the form if it's empty
                if (isFormEmpty()) {
                    loadSelectedTaskIntoForm();
                    JOptionPane.showMessageDialog(frame,
                            "Task loaded into form. Make changes and click 'Edit Task' again to save.",
                            "Edit Mode", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Form has data, so update the selected task
                    Task task = createTaskFromForm();
                    if (task != null && !task.getTitle().isEmpty()) {
                        List<Task> filteredTasks = getCurrentFilteredTasks();
                        if (selectedRow < filteredTasks.size()) {
                            Task originalTask = filteredTasks.get(selectedRow);
                            task.setId(originalTask.getId());
                            task.setCompleted(originalTask.isCompleted());
                            plannerService.updateTask(task);
                            clearForm();
                            loadTasksIntoTable();
                            JOptionPane.showMessageDialog(frame, "Task updated successfully!", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please enter a task title.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a task to edit.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to delete this task?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    List<Task> filteredTasks = getCurrentFilteredTasks();
                    if (selectedRow < filteredTasks.size()) {
                        Task taskToDelete = filteredTasks.get(selectedRow);
                        plannerService.deleteTask(taskToDelete.getId());
                        clearForm();
                        loadTasksIntoTable();
                        JOptionPane.showMessageDialog(frame, "Task deleted successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a task to delete.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == markCompleteButton) {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                List<Task> filteredTasks = getCurrentFilteredTasks();
                if (selectedRow < filteredTasks.size()) {
                    Task task = filteredTasks.get(selectedRow);
                    task.setCompleted(!task.isCompleted());
                    plannerService.updateTask(task);
                    loadTasksIntoTable();
                    JOptionPane.showMessageDialog(frame,
                            "Task marked as " + (task.isCompleted() ? "completed" : "pending") + "!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a task.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == searchButton) {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                List<Task> searchResults = plannerService.searchTasks(searchTerm);
                tableModel.setRowCount(0);
                for (Task task : searchResults) {
                    Object[] row = {
                            task.getTitle(),
                            task.getDescription(),
                            task.getDueDate() != null ? task.getDueDate().toString() : "",
                            task.getDueTime() != null ? task.getDueTime().toString() : "",
                            task.getPriority(),
                            task.isCompleted() ? "Yes" : "No",
                            task.getCategory()
                    };
                    tableModel.addRow(row);
                }
            } else {
                loadTasksIntoTable();
            }
        } else if (e.getSource() == refreshButton) {
            plannerService.loadTasks();
            searchField.setText("");
            loadTasksIntoTable();
        } else if (e.getSource() == emailSettingsButton) {
            new EmailSettingsPage(userID);
            frame.dispose();
        } else if (e.getSource() == filterComboBox || e.getSource() == showCompletedCheckbox) {
            loadTasksIntoTable();
        }
    }
}