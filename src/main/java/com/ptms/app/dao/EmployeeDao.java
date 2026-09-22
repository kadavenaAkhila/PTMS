package com.ptms.app.dao;

import com.ptms.app.model.Employee;

import java.util.List;

public interface EmployeeDao {
    int createEmployee(Employee employee);
    Employee getEmployeeById(int id);
    Employee getEmployeeByUserId(int userId);
    List<Employee> getAllEmployees();
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(int id);
}
