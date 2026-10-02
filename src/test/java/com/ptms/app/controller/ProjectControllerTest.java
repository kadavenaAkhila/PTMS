package com.ptms.app.controller;

import com.ptms.app.model.Project;
import com.ptms.app.model.User;
import com.ptms.app.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    private ProjectService projectService;
    private User manager;

    @BeforeEach
    void setUp() {
        projectService = mock(ProjectService.class);

        manager = new User("Max", "Manager", "mmanager", "mm@ptms.com", "hash", User.Role.PROJECT_MANAGER);
        manager.setId(2);
    }

    private ProjectController controllerWithInput(String... lines) {
        String simulatedInput = String.join("\n", lines) + "\n";
        return new ProjectController(projectService, new Scanner(simulatedInput));
    }

    @Test
    void showMenu_createProject() throws SQLException {
        // Arrange: "1", name, requirements, domain, cost, start date (blank), deadline (blank), priority, then "0"
        ProjectController controller = controllerWithInput(
                "1", "Portal", "Build a portal", "Retail", "50000", "", "", "HIGH", "0");

        // Act
        controller.showMenu(manager);

        // Assert
        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectService, times(1)).createProject(captor.capture(), org.mockito.ArgumentMatchers.eq(manager));
        assertEquals("Portal", captor.getValue().getName());
        assertEquals("HIGH", captor.getValue().getPriority());
    }

    @Test
    void showMenu_viewMyProjects() throws SQLException {
        // Arrange: "2", then "0"
        when(projectService.getProjectsForUser(manager)).thenReturn(java.util.List.of());
        ProjectController controller = controllerWithInput("2", "0");

        // Act
        controller.showMenu(manager);

        // Assert
        verify(projectService, times(1)).getProjectsForUser(manager);
    }

    @Test
    void showMenu_assignTeamLead() throws SQLException {
        // Arrange: "5", projectId, teamLeadId, then "0"
        ProjectController controller = controllerWithInput("5", "1", "3", "0");

        // Act
        controller.showMenu(manager);

        // Assert
        verify(projectService, times(1)).assignTeamLead(1, 3, manager);
    }

    @Test
    void showMenu_deleteProject() throws SQLException {
        // Arrange: "7", projectId, then "0"
        ProjectController controller = controllerWithInput("7", "1", "0");

        // Act
        controller.showMenu(manager);

        // Assert
        verify(projectService, times(1)).deleteProject(1, manager);
    }
}
