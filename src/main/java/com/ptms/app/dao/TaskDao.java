
package com.ptms.app.dao;

import com.ptms.app.model.Task;

import java.util.List;

public interface TaskDao {
    int createTask(Task task);
    Task getTaskById(int id);
    List<Task> getTasksByProject(int projectId);
    List<Task> getTasksByAssignee(int userId);
    List<Task> getAllTasks();
    boolean updateTask(Task task);
    boolean deleteTask(int id);
}