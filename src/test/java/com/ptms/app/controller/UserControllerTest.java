package com.ptms.app.controller;

import com.ptms.app.model.User;
import com.ptms.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private UserService userService;
    private User admin;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);

        admin = new User("Ada", "Admin", "aadmin", "a@ptms.com", "hash", User.Role.ADMIN);
        admin.setId(1);
    }

    private UserController controllerWithInput(String... lines) {
        String simulatedInput = String.join("\n", lines) + "\n";
        return new UserController(userService, new Scanner(simulatedInput));
    }

    @Test
    void registerUser() throws SQLException {
        // Arrange: first name, last name, username, email, password, role
        UserController controller = controllerWithInput(
                "Eli", "Emp", "eemp", "e@ptms.com", "hash123", "TEAM_MEMBER");
        when(userService.registerUser(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = controller.registerUser();

        // Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).registerUser(captor.capture());
        assertEquals("eemp", captor.getValue().getUsername());
        assertEquals(User.Role.TEAM_MEMBER, captor.getValue().getRole());
        assertEquals("eemp", result.getUsername());
    }

    @Test
    void login() throws SQLException {
        // Arrange
        UserController controller = controllerWithInput("aadmin", "hash");
        when(userService.login("aadmin", "hash")).thenReturn(admin);

        // Act
        User result = controller.login();

        // Assert
        assertEquals("aadmin", result.getUsername());
        verify(userService, times(1)).login("aadmin", "hash");
    }

    @Test
    void showMenu_changeRole() throws SQLException {
        // Arrange: choose "5", enter target user id, enter new role, then "0"
        UserController controller = controllerWithInput("5", "4", "TEAM_LEAD", "0");

        // Act
        controller.showMenu(admin);

        // Assert
        verify(userService, times(1)).changeRole(4, User.Role.TEAM_LEAD, admin);
    }

    @Test
    void showMenu_deleteUser() throws SQLException {
        // Arrange: choose "6", enter target user id, then "0"
        UserController controller = controllerWithInput("6", "4", "0");

        // Act
        controller.showMenu(admin);

        // Assert
        verify(userService, times(1)).deleteUser(4, admin);
    }

    @Test
    void showMenu_searchUsers() throws SQLException {
        // Arrange: choose "2", enter keyword, then "0"
        when(userService.searchUsers("Eli")).thenReturn(java.util.List.of());
        UserController controller = controllerWithInput("2", "Eli", "0");

        // Act
        controller.showMenu(admin);

        // Assert
        verify(userService, times(1)).searchUsers("Eli");
    }
}