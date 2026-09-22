package com.ptms.app.model;
import java.time.LocalDateTime;
public class TaskUpdate {
    private int id;
    private int taskId;
    private int updatedBy;
    private String status;
    private int progress;
    private String comment;
    private LocalDateTime createdAt;
    public TaskUpdate() {
    }
    public TaskUpdate(int id, int taskId, int updatedBy, String status, int progress,
                      String comment, LocalDateTime createdAt) {
        this.id = id;
        this.taskId = taskId;
        this.updatedBy = updatedBy;
        this.status = status;
        this.progress = progress;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public int getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
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
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @Override
    public String toString() {
        return "TaskUpdate{id=" + id + ", taskId=" + taskId + ", status='" + status + "', progress=" + progress + "}";
    }
}
