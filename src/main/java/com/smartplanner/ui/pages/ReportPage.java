package com.smartplanner.ui.pages;

import com.smartplanner.model.Task;
import com.smartplanner.model.TaskReport;
import com.smartplanner.service.PlannerService;
import com.smartplanner.ui.components.BasePage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Report page for viewing task statistics and categorized task lists.
 */
public class ReportPage extends BasePage implements ActionListener {
    private PlannerService plannerService;
    private JTabbedPane tabbedPane;

    public ReportPage(String userID) {
        this.userID = userID;
        this.plannerService = new PlannerService();
        initializeComponents();
        setupLayout();
        setupFrame();
    }

    private void initializeComponents() {
        frame = new JFrame("Smart Planner - Task Report");
        frame.setSize(900, 600);

        undoBackButton = new JButton("Back to Planner");
        undoBackButton.addActionListener(this);

        tabbedPane = new JTabbedPane();
    }

    private void setupLayout() {
        frame.setLayout(new BorderLayout());

        TaskReport report = plannerService.generateReport();

        // Statistics panel
        JPanel statsPanel = createStatsPanel(report);
        tabbedPane.addTab("Statistics", statsPanel);

        // Pending tasks tab
        JPanel pendingPanel = createTasksPanel(report.getPendingTasks(), "Pending Tasks");
        tabbedPane.addTab("Pending", pendingPanel);

        // Late tasks tab
        JPanel latePanel = createTasksPanel(report.getLateTasks(), "Late Tasks");
        tabbedPane.addTab("Overdue", latePanel);

        // Completed tasks tab
        JPanel completedPanel = createTasksPanel(report.getCompletedTasks(), "Completed Tasks");
        tabbedPane.addTab("Completed", completedPanel);

        // Bottom panel with back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(undoBackButton);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel(TaskReport report) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Task Statistics"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel titleLabel = new JLabel("Report Generated: " + report.getReportGeneratedDate());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Stats boxes
        gbc.gridwidth = 1;
        gbc.gridy = 1;

        addStatBox(panel, gbc, 0, "Total Tasks", String.valueOf(report.getTotalTasks()), new Color(100, 150, 200));
        addStatBox(panel, gbc, 1, "Pending", String.valueOf(report.getPendingTasks().size()), new Color(255, 193, 7));
        addStatBox(panel, gbc, 0, 2, "Overdue", String.valueOf(report.getLateTasks().size()), new Color(244, 67, 54));
        addStatBox(panel, gbc, 1, 2, "Completed", String.valueOf(report.getCompletedTasks().size()), new Color(76, 175, 80));

        // Completion percentage
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        JLabel percentLabel = new JLabel("Completion Rate: " + report.getCompletionPercentage() + "%");
        percentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        percentLabel.setForeground(new Color(76, 175, 80));
        panel.add(percentLabel, gbc);

        return panel;
    }

    private void addStatBox(JPanel panel, GridBagConstraints gbc, int x, int y, String label, String value, Color color) {
        gbc.gridx = x;
        gbc.gridy = y;
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(color);
        box.setPreferredSize(new Dimension(150, 100));
        box.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.BOLD, 12));
        labelComp.setForeground(Color.WHITE);
        labelComp.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.BOLD, 28));
        valueComp.setForeground(Color.WHITE);
        valueComp.setHorizontalAlignment(SwingConstants.CENTER);

        box.add(labelComp, BorderLayout.NORTH);
        box.add(valueComp, BorderLayout.CENTER);

        panel.add(box, gbc);
    }

    private void addStatBox(JPanel panel, GridBagConstraints gbc, int x, String label, String value, Color color) {
        addStatBox(panel, gbc, x, 1, label, value, color);
    }

    private JPanel createTasksPanel(java.util.List<Task> tasks, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        String[] columnNames = { "Title", "Description", "Due Date", "Due Time", "Priority", "Category" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Task task : tasks) {
            Object[] row = {
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate() != null ? task.getDueDate().toString() : "N/A",
                    task.getDueTime() != null ? task.getDueTime().toString() : "N/A",
                    task.getPriority(),
                    task.getCategory()
            };
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    @Override
    protected void setupFrame() {
        if (frame != null) {
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == undoBackButton) {
            frame.dispose();
        }
    }
}
