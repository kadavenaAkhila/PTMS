package com.ptms.app.model;
public class Admin {
    private int id;
    private int userId;
    private String accessLevel;
    public Admin() {
    }
    public Admin(int id, int userId, String accessLevel) {
        this.id = id;
        this.userId = userId;
        this.accessLevel = accessLevel;
    }
    // Convenience constructor for creating a new admin
    public Admin(int userId, String accessLevel) {
        this.userId = userId;
        this.accessLevel = accessLevel;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getAccessLevel() {
        return accessLevel;
    }
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
    @Override
    public String toString() {
        return "Admin{id=" + id + ", userId=" + userId + ", accessLevel='" + accessLevel + "'}";
    }
}
