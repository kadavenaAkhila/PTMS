package com.ptms.app.dao;

import com.ptms.app.model.Admin;

import java.util.List;

public interface AdminDao {
    int createAdmin(Admin admin);
    Admin getAdminById(int id);
    Admin getAdminByUserId(int userId);
    List<Admin> getAllAdmins();
    boolean updateAdmin(Admin admin);
    boolean deleteAdmin(int id);
}
