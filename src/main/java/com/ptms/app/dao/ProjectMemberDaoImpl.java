package com.ptms.app.dao;

import com.ptms.app.model.ProjectMember;
import com.ptms.app.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectMemberDaoImpl implements ProjectMemberDao {

    @Override
    public boolean addMember(int projectId, int userId) {
        // joined_at is left out here — let MySQL fill it with a DEFAULT CURRENT_TIMESTAMP
        String sql = "INSERT INTO project_members (project_id, user_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeMember(int projectId, int userId) {
        String sql = "DELETE FROM project_members WHERE project_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ProjectMember> getMembersByProject(int projectId) {
        List<ProjectMember> members = new ArrayList<>();
        String sql = "SELECT * FROM project_members WHERE project_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) members.add(mapRowToMember(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public List<ProjectMember> getProjectsByUser(int userId) {
        List<ProjectMember> memberships = new ArrayList<>();
        String sql = "SELECT * FROM project_members WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) memberships.add(mapRowToMember(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberships;
    }

    @Override
    public boolean isMember(int projectId, int userId) {
        String sql = "SELECT 1 FROM project_members WHERE project_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ProjectMember mapRowToMember(ResultSet rs) throws SQLException {
        return new ProjectMember(
                rs.getInt("project_id"),
                rs.getInt("user_id"),
                rs.getTimestamp("joined_at").toLocalDateTime()
        );
    }
}
