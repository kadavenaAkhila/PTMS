package com.ptms.app.dao;

import com.ptms.app.model.TaskUpdate;

import java.util.List;

public interface TaskUpdateDao {
    int createTaskUpdate(TaskUpdate update);
    TaskUpdate getTaskUpdateById(int id);
    List<TaskUpdate> getUpdatesByTask(int taskId);
    List<TaskUpdate> getAllTaskUpdates();
    boolean deleteTaskUpdate(int id);
}
