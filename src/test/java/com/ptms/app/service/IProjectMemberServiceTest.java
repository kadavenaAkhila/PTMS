package com.ptms.app.service;

import com.ptms.app.dao.ProjectDao;
import com.ptms.app.dao.ProjectMemberDao;
import com.ptms.app.dao.UserDao;
import com.ptms.app.exception.UnauthorizedException;
import com.ptms.app.exception.ValidationException;
import com.ptms.app.model.Project;
import com.ptms.app.model.ProjectMember;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IProjectMemberServiceTest {

    private ProjectMemberDao projectMemberDao;
    private ProjectDao projectDao;
    private UserDao userDao;
    private ProjectMemberService projectMemberService;

    private User manager;
    private User outsider;
    private Project project;

    @BeforeEach
    void setUp() {
        projectMemberDao = mock(ProjectMemberDao.class);
        projectDao = mock(ProjectDao.class);
        userDao = mock(UserDao.class);
        projectMemberService = new IProjectMemberService(projectMemberDao, projectDao, userDao);

        manager = new User("Max", "Manager", "mmanager", "mm@ptms.com", "hash", User.Role.PROJECT_MANAGER);
        manager.setId(2);

        outsider = new User("Eli", "Emp", "eemp", "e@ptms.com", "hash", User.Role.TEAM_MEMBER);
        outsider.setId(4);

        project = new Project("Portal", "reqs", 2, "HIGH");
        project.setId(1);
    }

    @Test
    void addMember_ShouldThrowUnauthorizedExceptionForNonProjectManager() throws SQLException {
        // Arrange
        when(projectDao.findById(1)).thenReturn(project);

        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> projectMemberService.addMember(1, 5, "TEAM_MEMBER", outsider));
    }

    @Test
    void addMember_ShouldThrowValidationExceptionForExistingMember() throws SQLException {
        // Arrange
        when(projectDao.findById(1)).thenReturn(project);
        when(userDao.findById(4)).thenReturn(outsider);
        when(projectMemberDao.findByProjectId(1))
                .thenReturn(List.of(new ProjectMember(1, 4, "TEAM_MEMBER")));

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> projectMemberService.addMember(1, 4, "TEAM_MEMBER", manager));
    }

    @Test
    void removeMember() throws SQLException {
        // Arrange
        when(projectDao.findById(1)).thenReturn(project);
        when(projectMemberDao.delete(1, 4)).thenReturn(1);

        // Act
        projectMemberService.removeMember(1, 4, manager);

        // Assert
        verify(projectMemberDao, times(1)).delete(1, 4);
    }
}
