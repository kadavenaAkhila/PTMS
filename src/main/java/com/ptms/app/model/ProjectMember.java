package com.ptms.app.model;
import java.time.LocalDateTime;
public class ProjectMember {
    private int projectId;
    private int userId;
    private LocalDateTime joinedAt;
    public ProjectMember() {
    }
    public ProjectMember(int projectId, int userId, LocalDateTime joinedAt) {
        this.projectId = projectId;
        this.userId = userId;
        this.joinedAt = joinedAt;
    }
    public ProjectMember(int projectId, int userId) {
        this.projectId = projectId;
        this.userId = userId;
    }
    public int getProjectId() {
        return projectId;
    }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
    @Override
    public String toString() {
        return "ProjectMember{projectId=" + projectId + ", userId=" + userId + ", joinedAt=" + joinedAt + "}";
    }
}