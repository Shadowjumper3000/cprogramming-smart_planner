package com.smartplanner.model;

import java.time.LocalDate;
import java.util.List;

public class TaskReport {
    private List<Task> pendingTasks;
    private List<Task> lateTasks;
    private List<Task> completedTasks;
    private LocalDate reportGeneratedDate;

    public TaskReport(List<Task> pendingTasks, List<Task> lateTasks, List<Task> completedTasks) {
        this.pendingTasks = pendingTasks;
        this.lateTasks = lateTasks;
        this.completedTasks = completedTasks;
        this.reportGeneratedDate = LocalDate.now();
    }

    public List<Task> getPendingTasks() {
        return pendingTasks;
    }

    public List<Task> getLateTasks() {
        return lateTasks;
    }

    public List<Task> getCompletedTasks() {
        return completedTasks;
    }

    public LocalDate getReportGeneratedDate() {
        return reportGeneratedDate;
    }

    public int getTotalTasks() {
        return pendingTasks.size() + lateTasks.size() + completedTasks.size();
    }

    public int getCompletionPercentage() {
        int total = getTotalTasks();
        return total == 0 ? 0 : (completedTasks.size() * 100) / total;
    }
}
