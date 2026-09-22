package com.ptms.app.dao;

import com.ptms.app.model.Task;
import com.ptms.app.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDaoImpl implements TaskDao {

    @Override
    public int createTask(Task task) {
        String sql = "INSERT INTO tasks (project_id, assigned_to, title, description, priority, "
                + "deadline, status, progress) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, task.getProjectId());
            ps.setInt(2, task.getAssignedTo());
            ps.setString(3, task.getTitle());
            ps.setString(4, task.getDescription());
            ps.setString(5, task.getPriority());
            ps.setDate(6, task.getDeadline() != null ? Date.valueOf(task.getDeadline()) : null);
            ps.setString(7, task.getStatus());
            ps.setInt(8, task.getProgress());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Task getTaskById(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToTask(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Task> getTasksByProject(int projectId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE project_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tasks.add(mapRowToTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByAssignee(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE assigned_to = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tasks.add(mapRowToTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) tasks.add(mapRowToTask(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET project_id = ?, assigned_to = ?, title = ?, description = ?, "
                + "priority = ?, deadline = ?, status = ?, progress = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, task.getProjectId());
            ps.setInt(2, task.getAssignedTo());
            ps.setString(3, task.getTitle());
            ps.setString(4, task.getDescription());
            ps.setString(5, task.getPriority());
            ps.setDate(6, task.getDeadline() != null ? Date.valueOf(task.getDeadline()) : null);
            ps.setString(7, task.getStatus());
            ps.setInt(8, task.getProgress());
            ps.setInt(9, task.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Task mapRowToTask(ResultSet rs) throws SQLException {
        Date d = rs.getDate("deadline");
        return new Task(
                rs.getInt("id"), rs.getInt("project_id"), rs.getInt("assigned_to"),
                rs.getString("title"), rs.getString("description"), rs.getString("priority"),
                d != null ? d.toLocalDate() : null, rs.getString("status"), rs.getInt("progress")
        );
    }
}
