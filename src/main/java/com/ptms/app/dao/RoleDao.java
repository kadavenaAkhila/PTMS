package com.ptms.app.dao;

import com.ptms.app.model.Role;

import java.util.List;

public interface RoleDao {
    int createRole(Role role);
    Role getRoleById(int id);
    List<Role> getAllRoles();
    boolean updateRole(Role role);
    boolean deleteRole(int id);
}