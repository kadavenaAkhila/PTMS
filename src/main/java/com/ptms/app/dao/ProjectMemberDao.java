package com.ptms.app.dao;

import com.ptms.app.model.ProjectMember;

import java.util.List;

// Note: no single-id create/get/update/delete here, since project_members
// has a composite key (project_id + user_id) instead of its own id column.
public interface ProjectMemberDao {
    boolean addMember(int projectId, int userId);
    boolean removeMember(int projectId, int userId);
    List<ProjectMember> getMembersByProject(int projectId);
    List<ProjectMember> getProjectsByUser(int userId);
    boolean isMember(int projectId, int userId);
}