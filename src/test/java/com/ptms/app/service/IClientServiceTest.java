package com.ptms.app.service;

import com.ptms.app.dao.ClientDao;
import com.ptms.app.exception.ResourceNotFoundException;
import com.ptms.app.exception.UnauthorizedException;
import com.ptms.app.model.Client;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IClientServiceTest {

    private ClientDao clientDao;
    private ClientService clientService;
    private User admin;
    private User employee;
    private Client client;

    @BeforeEach
    void setUp() {
        clientDao = org.mockito.Mockito.mock(ClientDao.class);
        clientService = new IClientService(clientDao);

        admin = new User("Ada", "Admin", "aadmin", "a@ptms.com", "hash", User.Role.ADMIN);
        admin.setId(1);

        employee = new User("Eli", "Emp", "eemp", "e@ptms.com", "hash", User.Role.TEAM_MEMBER);
        employee.setId(4);

        client = new Client("Acme", "j@acme.com", "999", "Acme Corp");
    }

    @Test
    void addClient() throws SQLException {
        // Arrange
        when(clientDao.insert(client)).thenReturn(1);

        // Act
        Client result = clientService.addClient(client, admin);

        // Assert
        assertNotNull(result);
        assertEquals(client, result);
        verify(clientDao, times(1)).insert(client);
    }

    @Test
    void addClient_ShouldThrowUnauthorizedExceptionForEmployee() {
        // Arrange & Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> clientService.addClient(client, employee));
    }

    @Test
    void updateClient() throws SQLException {
        // Arrange
        client.setId(1);
        when(clientDao.update(client)).thenReturn(1);

        // Act
        clientService.updateClient(client, admin);

        // Assert
        verify(clientDao, times(1)).update(client);
    }

    @Test
    void updateClient_ShouldThrowNotFoundWhenNoRowsAffected() throws SQLException {
        // Arrange
        client.setId(99);
        when(clientDao.update(client)).thenReturn(0);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> clientService.updateClient(client, admin));
    }

    @Test
    void updateClient_ShouldThrowUnauthorizedExceptionForEmployee() {
        // Arrange & Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> clientService.updateClient(client, employee));
    }

    @Test
    void deleteClient() throws SQLException {
        // Arrange
        when(clientDao.delete(1)).thenReturn(1);

        // Act
        clientService.deleteClient(1, admin);

        // Assert
        verify(clientDao, times(1)).delete(1);
    }

    @Test
    void deleteClient_ShouldThrowNotFoundWhenNoRowsAffected() throws SQLException {
        // Arrange
        when(clientDao.delete(99)).thenReturn(0);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> clientService.deleteClient(99, admin));
    }

    @Test
    void deleteClient_ShouldThrowUnauthorizedExceptionForEmployee() {
        // Arrange & Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> clientService.deleteClient(1, employee));
    }

    @Test
    void getClientById() throws SQLException {
        // Arrange
        client.setId(1);
        when(clientDao.findById(1)).thenReturn(client);

        // Act
        Client result = clientService.getClientById(1);

        // Assert
        assertEquals(client, result);
    }

    @Test
    void name() {
    }

    @Test
    void getClientById_ShouldThrowNotFoundWhenMissing() throws SQLException {
        // Arrange
        when(clientDao.findById(99)).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(99));
    }

    @Test
    void searchClientByName() throws SQLException {
        // Arrange
        when(clientDao.searchByName("Acme")).thenReturn(java.util.List.of(client));

        // Act
        var results = clientService.searchClients("Acme");

        // Assert
        assertEquals(1, results.size());
    }
}
