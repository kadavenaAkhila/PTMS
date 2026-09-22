package com.ptms.app.dao;

import com.ptms.app.model.Role;
import com.ptms.app.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {

    @Override
    public int createRole(Role role) {
        String sql = "INSERT INTO roles (role_name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, role.getRoleName());
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
    public Role getRoleById(int id) {
        String sql = "SELECT * FROM roles WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToRole(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.add(mapRowToRole(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public boolean updateRole(Role role) {
        String sql = "UPDATE roles SET role_name = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role.getRoleName());
            ps.setInt(2, role.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteRole(int id) {
        String sql = "DELETE FROM roles WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Role mapRowToRole(ResultSet rs) throws SQLException {
        return new Role(rs.getInt("id"), rs.getString("role_name"));
    }

    public static void main(String[] args) {
        RoleDao dao = new RoleDaoImpl();
        int id = dao.createRole(new Role(0, "Project Manager"));
        System.out.println("Created role id: " + id);
        System.out.println("Fetched: " + dao.getRoleById(id));
        System.out.println("Total roles: " + dao.getAllRoles().size());
    }
}
