package com.ptms.app.dao;

import com.ptms.app.model.TaskUpdate;
import com.ptms.app.util.DBConnection;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Note: task_updates is a history/log table — rows are created and read,
// but typically never updated (each new update is a new row instead).
public class TaskUpdateDaoImpl implements TaskUpdateDao {

    @Override
    public int createTaskUpdate(TaskUpdate update) {
        String sql = "INSERT INTO task_updates (task_id, updated_by, status, progress, comment) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, update.getTaskId());
            ps.setInt(2, update.getUpdatedBy());
            ps.setString(3, update.getStatus());
            ps.setInt(4, update.getProgress());
            ps.setString(5, update.getComment());
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
    public TaskUpdate getTaskUpdateById(int id) {
        String sql = "SELECT * FROM task_updates WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToTaskUpdate(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TaskUpdate> getUpdatesByTask(int taskId) {
        List<TaskUpdate> updates = new ArrayList<>();
        String sql = "SELECT * FROM task_updates WHERE task_id = ? ORDER BY created_at";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) updates.add(mapRowToTaskUpdate(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updates;
    }

    @Override
    public List<TaskUpdate> getAllTaskUpdates() {
        List<TaskUpdate> updates = new ArrayList<>();
        String sql = "SELECT * FROM task_updates";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) updates.add(mapRowToTaskUpdate(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updates;
    }

    @Override
    public boolean deleteTaskUpdate(int id) {
        String sql = "DELETE FROM task_updates WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private TaskUpdate mapRowToTaskUpdate(ResultSet rs) throws SQLException {
        return new TaskUpdate(
                rs.getInt("id"), rs.getInt("task_id"), rs.getInt("updated_by"),
                rs.getString("status"), rs.getInt("progress"), rs.getString("comment"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
