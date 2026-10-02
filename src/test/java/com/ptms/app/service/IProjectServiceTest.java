package com.ptms.app.service;

import com.ptms.app.dao.ProjectDao;
import com.ptms.app.dao.ProjectMemberDao;
import com.ptms.app.dao.UserDao;
import com.ptms.app.exception.UnauthorizedException;
import com.ptms.app.exception.ValidationException;
import com.ptms.app.model.Project;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IProjectServiceTest {

    private ProjectDao projectDao;
    private ProjectMemberDao projectMemberDao;
    private UserDao userDao;
    private ProjectService projectService;

    private User manager;
    private User teamMember;

    @BeforeEach
    void setUp() {
        projectDao = mock(ProjectDao.class);
        projectMemberDao = mock(ProjectMemberDao.class);
        userDao = mock(UserDao.class);
        projectService = new IProjectService(projectDao, projectMemberDao, userDao);

        manager = new User("Max", "Manager", "mmanager", "mm@ptms.com", "hash", User.Role.PROJECT_MANAGER);
        manager.setId(2);

        teamMember = new User("Eli", "Emp", "eemp", "e@ptms.com", "hash", User.Role.TEAM_MEMBER);
        teamMember.setId(4);
    }

    @Test
    void createProject_ShouldThrowUnauthorizedExceptionForEmployee() {
        // Arrange
        Project project = new Project("Portal", "reqs", null, "HIGH");

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> projectService.createProject(project, teamMember));
    }

    @Test
    void createProject_ShouldDefaultManagerIdToCreator() throws SQLException {
        // Arrange
        Project project = new Project("Portal", "reqs", null, "HIGH");

        // Act
        projectService.createProject(project, manager);

        // Assert
        assertEquals(2, project.getManagerId());
        verify(projectDao, times(1)).insert(project);
    }

    @Test
    void assignTeamLead_ShouldThrowValidationExceptionForWrongRole() throws SQLException {
        // Arrange
        // Note: no projectDao.findById() stub here — the service checks the
        // candidate's role and throws before it ever looks up the project,
        // so stubbing that call would be unused and Mockito would flag it.
        when(userDao.findById(4)).thenReturn(teamMember); // TEAM_MEMBER, not TEAM_LEAD

        // Act & Assert
        assertThrows(ValidationException.class, () -> projectService.assignTeamLead(1, 4, manager));
    }

    @Test
    void assignTeamLead_ShouldThrowUnauthorizedExceptionForNonManager() {
        // Arrange & Act & Assert
        assertThrows(UnauthorizedException.class, () -> projectService.assignTeamLead(1, 3, teamMember));
    }

    @Test
    void getProjectsForUser_ShouldReturnAllProjectsForAdmin() throws SQLException {
        // Arrange
        User admin = new User("Ada", "Admin", "aadmin", "a@ptms.com", "hash", User.Role.ADMIN);
        when(projectDao.findAll()).thenReturn(Collections.emptyList());

        // Act
        projectService.getProjectsForUser(admin);

        // Assert
        verify(projectDao, times(1)).findAll();
    }

    @Test
    void getProjectsForUser_ShouldReturnManagedProjectsForManager() throws SQLException {
        // Arrange
        when(projectDao.findByManagerId(2)).thenReturn(Collections.emptyList());

        // Act
        projectService.getProjectsForUser(manager);

        // Assert
        verify(projectDao, times(1)).findByManagerId(2);
    }

    @Test
    void deleteProject_ShouldThrowUnauthorizedExceptionForNonAdmin() {
        // Arrange & Act & Assert
        assertThrows(UnauthorizedException.class, () -> projectService.deleteProject(1, manager));
    }
}
