package com.ptms.app.model;
import java.math.BigDecimal;
import java.time.LocalDate;
public class Project {
    private int id;
    private String name;
    private String requirements;
    private int managerId;
    private int teamLeadId;
    private int clientId;
    private String domain;
    private BigDecimal cost;
    private int teamSize;
    private LocalDate startDate;
    private LocalDate deadline;
    private String priority;
    private String status;
    public Project() {
    }
    public Project(int id, String name, String requirements, int managerId, int teamLeadId,
                   int clientId, String domain, BigDecimal cost, int teamSize,
                   LocalDate startDate, LocalDate deadline, String priority, String status) {
        this.id = id;
        this.name = name;
        this.requirements = requirements;
        this.managerId = managerId;
        this.teamLeadId = teamLeadId;
        this.clientId = clientId;
        this.domain = domain;
        this.cost = cost;
        this.teamSize = teamSize;
        this.startDate = startDate;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRequirements() {
        return requirements;
    }
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    public int getManagerId() {
        return managerId;
    }
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
    public int getTeamLeadId() {
        return teamLeadId;
    }
    public void setTeamLeadId(int teamLeadId) {
        this.teamLeadId = teamLeadId;
    }
    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public BigDecimal getCost() {
        return cost;
    }
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    public int getTeamSize() {
        return teamSize;
    }
    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getDeadline() {
        return deadline;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "Project{id=" + id + ", name='" + name + "', status='" + status + "', priority='" + priority + "'}";
    }
}