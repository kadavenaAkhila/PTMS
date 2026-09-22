package com.ptms.app.dao;

import com.ptms.app.model.User;

import java.util.List;

public interface UserDao {

    int createUser(User user);

    User getUserById(int id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    List<User> searchUsers(String keyword);

    boolean updateUser(User user);

    boolean deleteUser(int id);
}