package com.ptms.app.dao;

import com.ptms.app.model.Project;

import java.util.List;

public interface ProjectDao {

    boolean addProject(Project project);

    boolean updateProject(Project project);

    boolean deleteProject(int projectId);

    Project getProjectById(int projectId);

    List<Project> getAllProjects();

    List<Project> searchProjects(String keyword);

    List<Project> getProjectsByManager(int managerId);

    List<Project> getProjectsByClient(int clientId);
}
