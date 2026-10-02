package com.ptms.app.controller;

import com.ptms.app.model.User;
import com.ptms.app.service.ProjectMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Scanner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectMemberControllerTest {

    private ProjectMemberService projectMemberService;
    private User manager;

    @BeforeEach
    void setUp() {
        projectMemberService = mock(ProjectMemberService.class);

        manager = new User("Max", "Manager", "mmanager", "mm@ptms.com", "hash", User.Role.PROJECT_MANAGER);
        manager.setId(2);
    }

    private ProjectMemberController controllerWithInput(String... lines) {
        String simulatedInput = String.join("\n", lines) + "\n";
        return new ProjectMemberController(projectMemberService, new Scanner(simulatedInput));
    }

    @Test
    void showMenu_addMember() throws SQLException {
        // Arrange: "1", projectId, userId, role, then "0"
        ProjectMemberController controller = controllerWithInput("1", "1", "4", "TEAM_MEMBER", "0");

        // Act
        controller.showMenu(manager);

        // Assert
        verify(projectMemberService, times(1)).addMember(1, 4, "TEAM_MEMBER", manager);
    }

    @Test
    void showMenu_removeMember() throws SQLException {
        // Arrange: "2", projectId, userId, then "0"
        ProjectMemberController controller = controllerWithInput("2", "1", "4", "0");

        // Act
        controller.showMenu(manager);

        // Assert
        verify(projectMemberService, times(1)).removeMember(1, 4, manager);
    }

    @Test
    void showMenu_viewMembersOfProject() throws SQLException {
        // Arrange: "3", projectId, then "0"
        when(projectMemberService.getMembersOfProject(1)).thenReturn(java.util.List.of());
        ProjectMemberController controller = controllerWithInput("3", "1", "0");

        // Act
        controller.showMenu(manager);

        // Assert
        verify(projectMemberService, times(1)).getMembersOfProject(1);
    }

    @Test
    void showMenu_viewMyMemberships() throws SQLException {
        // Arrange: "4", then "0"
        when(projectMemberService.getProjectsForMember(2)).thenReturn(java.util.List.of());
        ProjectMemberController controller = controllerWithInput("4", "0");

        // Act
        controller.showMenu(manager);

        // Assert
        verify(projectMemberService, times(1)).getProjectsForMember(2);
    }
}
