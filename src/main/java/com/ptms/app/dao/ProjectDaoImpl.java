package com.ptms.app.dao;

import com.ptms.app.model.Project;
import com.ptms.app.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDaoImpl implements ProjectDao {

    @Override
    public boolean addProject(Project project) {
        String sql = "INSERT INTO projects (name, requirements, manager_id, team_lead_id, client_id, " +
                "domain, cost, team_size, start_date, deadline, priority, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            bindProject(ps, project, false);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateProject(Project project) {
        String sql = "UPDATE projects SET name = ?, requirements = ?, manager_id = ?, team_lead_id = ?, " +
                "client_id = ?, domain = ?, cost = ?, team_size = ?, start_date = ?, deadline = ?, " +
                "priority = ?, status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            bindProject(ps, project, true);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProject(int projectId) {
        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Project getProjectById(int projectId) {
        String sql = "SELECT * FROM projects WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                projects.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    @Override
    public List<Project> searchProjects(String keyword) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE name LIKE ? OR domain LIKE ? OR status LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    projects.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    @Override
    public List<Project> getProjectsByManager(int managerId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE manager_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, managerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    projects.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    @Override
    public List<Project> getProjectsByClient(int clientId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE client_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    projects.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    private void bindProject(PreparedStatement ps, Project project, boolean includeId) throws SQLException {
        ps.setString(1, project.getName());
        ps.setString(2, project.getRequirements());
        ps.setInt(3, project.getManagerId());
        ps.setInt(4, project.getTeamLeadId());
        ps.setInt(5, project.getClientId());
        ps.setString(6, project.getDomain());
        ps.setBigDecimal(7, project.getCost());
        ps.setInt(8, project.getTeamSize());
        ps.setDate(9, project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null);
        ps.setDate(10, project.getDeadline() != null ? Date.valueOf(project.getDeadline()) : null);
        ps.setString(11, project.getPriority());
        ps.setString(12, project.getStatus());

        if (includeId) {
            ps.setInt(13, project.getId());
        }
    }

    private Project mapRow(ResultSet rs) throws SQLException {
        Date startDate = rs.getDate("start_date");
        Date deadline = rs.getDate("deadline");
        BigDecimal cost = rs.getBigDecimal("cost");

        return new Project(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("requirements"),
                rs.getInt("manager_id"),
                rs.getInt("team_lead_id"),
                rs.getInt("client_id"),
                rs.getString("domain"),
                cost,
                rs.getInt("team_size"),
                startDate != null ? startDate.toLocalDate() : null,
                deadline != null ? deadline.toLocalDate() : null,
                rs.getString("priority"),
                rs.getString("status")
        );
    }
}
