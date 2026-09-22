package com.ptms.app.model;

import java.time.LocalDate;
public class Task {
    private int id;
    private int projectId;
    private Integer assignedTo;
    private String title;
    private String description;
    private String priority;
    private LocalDate deadline;
    private String status;
    private int progress;
    public Task() {
    }
    public Task(int id, int projectId, Integer assignedTo, String title, String description,
                String priority, LocalDate deadline, String status, int progress) {
        this.id = id;
        this.projectId = projectId;
        this.assignedTo = assignedTo;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
        this.progress = progress;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getProjectId() {
        return projectId;
    }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    public Integer getAssignedTo() {
        return assignedTo;
    }
    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public LocalDate getDeadline() {
        return deadline;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', status='" + status + "', progress=" + progress + "}";
    }
}