package com.ptms.app.service;
import com.ptms.app.dao.UserDao;
import com.ptms.app.exception.UnauthorizedException;
import com.ptms.app.exception.ValidationException;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class IUserServiceTest {
    private UserDao userDao;
    private UserService userService;
    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        userService = new IUserService(userDao);
    }
    @Test
    void registerUser() throws SQLException {
        // Arrange
        User newUser = new User("Eli", "Emp", "eemp", "eemp@ptms.com", "hash", User.Role.TEAM_MEMBER);
        when(userDao.findByUsername("eemp")).thenReturn(null);
        when(userDao.insert(newUser)).thenReturn(1);
        // Act
        User result = userService.registerUser(newUser);
        // Assert
        assertEquals("eemp", result.getUsername());
        verify(userDao, times(1)).insert(newUser);
    }
    @Test
    void registerUser_ShouldThrowValidationExceptionForDuplicateUsername() throws SQLException {
        // Arrange
        User existing = new User("Old", "User", "eemp", "old@ptms.com", "hash", User.Role.TEAM_MEMBER);
        User newUser = new User("Eli", "Emp", "eemp", "eemp@ptms.com", "hash", User.Role.TEAM_MEMBER);
        when(userDao.findByUsername("eemp")).thenReturn(existing);

        // Act & Assert
        assertThrows(ValidationException.class, () -> userService.registerUser(newUser));
        verify(userDao, never()).insert(any());
    }
    @Test
    void login() throws SQLException {
        // Arrange
        User stored = new User("Eli", "Emp", "eemp", "eemp@ptms.com", "correct-hash", User.Role.TEAM_MEMBER);
        when(userDao.findByUsername("eemp")).thenReturn(stored);
        // Act
        User result = userService.login("eemp", "correct-hash");
        // Assert
        assertEquals("eemp", result.getUsername());
    }
    @Test
    void login_ShouldThrowValidationExceptionForWrongPassword() throws SQLException {
        // Arrange
        User stored = new User("Eli", "Emp", "eemp", "eemp@ptms.com", "correct-hash", User.Role.TEAM_MEMBER);
        when(userDao.findByUsername("eemp")).thenReturn(stored);

        // Act & Assert
        assertThrows(ValidationException.class, () -> userService.login("eemp", "wrong-hash"));
    }
    @Test
    void changeRole_ShouldThrowUnauthorizedExceptionForNonAdmin() {
        // Arrange
        User requestingUser = new User("Max", "Manager", "mmanager", "mm@ptms.com", "hash", User.Role.PROJECT_MANAGER);

        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> userService.changeRole(5, User.Role.TEAM_LEAD, requestingUser));
    }
    @Test
    void changeRole() throws SQLException {
        // Arrange
        User admin = new User("Ada", "Admin", "aadmin", "a@ptms.com", "hash", User.Role.ADMIN);
        User target = new User("Eli", "Emp", "eemp", "e@ptms.com", "hash", User.Role.TEAM_MEMBER);
        target.setId(5);
        when(userDao.findById(5)).thenReturn(target);
        when(userDao.update(any())).thenReturn(1);
        // Act
        userService.changeRole(5, User.Role.TEAM_LEAD, admin);
        // Assert
        assertEquals(User.Role.TEAM_LEAD, target.getRole());
        verify(userDao, times(1)).update(target);
    }
}
