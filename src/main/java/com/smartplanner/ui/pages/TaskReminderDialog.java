package com.smartplanner.ui.pages;

import com.smartplanner.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Dialog for adding or editing a task with email reminder options.
 */
public class TaskReminderDialog extends JDialog implements ActionListener {
    private JTextField titleField;
    private JTextField descriptionField;
    private JTextField dueDateField;
    private JTextField dueTimeField;
    private JTextField categoryField;
    private JComboBox<String> priorityComboBox;
    private JCheckBox reminderCheckbox;
    private JComboBox<Integer> reminderMinutesComboBox;
    
    private JButton saveButton;
    private JButton cancelButton;
    
    private Task task;
    private boolean saved = false;

    /**
     * Creates a dialog for a new task.
     */
    public TaskReminderDialog(Frame parent) {
        this(parent, null);
    }

    /**
     * Creates a dialog for editing an existing task.
     */
    public TaskReminderDialog(Frame parent, Task existingTask) {
        super(parent, existingTask == null ? "Add Task" : "Edit Task", true);
        this.task = existingTask != null ? existingTask : new Task();
        initializeComponents();
        setupLayout();
        if (existingTask != null) {
            loadTaskData();
        }
        setLocationRelativeTo(parent);
        pack();
    }

    private void initializeComponents() {
        titleField = new JTextField(20);
        descriptionField = new JTextField(20);
        dueDateField = new JTextField(10);
        dueTimeField = new JTextField(8);
        categoryField = new JTextField(15);

        String[] priorities = {"High", "Medium", "Low"};
        priorityComboBox = new JComboBox<>(priorities);
        priorityComboBox.setSelectedItem("Medium");

        reminderCheckbox = new JCheckBox("Enable Email Reminder");
        
        Integer[] reminderOptions = {5, 10, 15, 30, 60, 120, 180, 360, 720, 1440}; // Minutes
        reminderMinutesComboBox = new JComboBox<>(reminderOptions);
        reminderMinutesComboBox.setSelectedItem(30); // Default 30 minutes

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        reminderCheckbox.addActionListener(e -> 
            reminderMinutesComboBox.setEnabled(reminderCheckbox.isSelected())
        );
        reminderMinutesComboBox.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(descriptionField, gbc);

        // Due Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Due Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        mainPanel.add(dueDateField, gbc);

        // Due Time
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Due Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        mainPanel.add(dueTimeField, gbc);

        // Priority
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(priorityComboBox, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(categoryField, gbc);

        // Reminder checkbox
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        mainPanel.add(reminderCheckbox, gbc);

        // Reminder time
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Remind before (minutes):"), gbc);
        gbc.gridx = 1;
        mainPanel.add(reminderMinutesComboBox, gbc);

        // Info label
        JLabel infoLabel = new JLabel("<html><i>Configure email settings in Settings menu</i></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        mainPanel.add(infoLabel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadTaskData() {
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        
        if (task.getDueDate() != null) {
            dueDateField.setText(task.getDueDate().toString());
        }
        
        if (task.getDueTime() != null) {
            dueTimeField.setText(task.getDueTime().toString());
        }
        
        priorityComboBox.setSelectedItem(task.getPriority());
        categoryField.setText(task.getCategory());
        
        reminderCheckbox.setSelected(task.isEmailReminderEnabled());
        reminderMinutesComboBox.setSelectedItem(task.getReminderMinutesBefore());
        reminderMinutesComboBox.setEnabled(task.isEmailReminderEnabled());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            if (validateAndSave()) {
                saved = true;
                dispose();
            }
        } else if (e.getSource() == cancelButton) {
            saved = false;
            dispose();
        }
    }

    private boolean validateAndSave() {
        // Validate title
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Title is required!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate and parse due date
        LocalDate dueDate = null;
        String dueDateText = dueDateField.getText().trim();
        if (!dueDateText.isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueDateText);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid date format. Use YYYY-MM-DD",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Validate and parse due time
        LocalTime dueTime = null;
        String dueTimeText = dueTimeField.getText().trim();
        if (!dueTimeText.isEmpty()) {
            try {
                dueTime = LocalTime.parse(dueTimeText);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid time format. Use HH:MM",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Validate reminder settings
        if (reminderCheckbox.isSelected()) {
            if (dueDate == null || dueTime == null) {
                JOptionPane.showMessageDialog(this,
                        "Due date and time are required for email reminders!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Update task
        task.setTitle(title);
        task.setDescription(descriptionField.getText().trim());
        task.setDueDate(dueDate);
        task.setDueTime(dueTime);
        task.setPriority((String) priorityComboBox.getSelectedItem());
        task.setCategory(categoryField.getText().trim());
        task.setEmailReminderEnabled(reminderCheckbox.isSelected());
        task.setReminderMinutesBefore((Integer) reminderMinutesComboBox.getSelectedItem());

        return true;
    }

    public Task getTask() {
        return task;
    }

    public boolean isSaved() {
        return saved;
    }
}
