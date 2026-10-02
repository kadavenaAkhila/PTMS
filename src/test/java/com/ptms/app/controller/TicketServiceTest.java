package com.ptms.app.controller;

import com.ptms.app.model.Ticket;
import com.ptms.app.model.User;
import com.ptms.app.service.TicketService;
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

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    private TicketService ticketService;
    private User teamLead;

    @BeforeEach
    void setUp() {
        ticketService = mock(TicketService.class);

        teamLead = new User("Tara", "Lead", "tlead", "t@ptms.com", "hash", User.Role.TEAM_LEAD);
        teamLead.setId(3);
    }

    private TicketController controllerWithInput(String... lines) {
        String simulatedInput = String.join("\n", lines) + "\n";
        return new TicketController(ticketService, new Scanner(simulatedInput));
    }

    @Test
    void showMenu_createTicket() throws SQLException {
        // Arrange: "1", projectId, title, description, priority, deadline (blank), then "0"
        TicketController controller = controllerWithInput(
                "1", "1", "Set up DB", "Create tables", "HIGH", "", "0");

        // Act
        controller.showMenu(teamLead);

        // Assert
        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketService, times(1)).createTicket(captor.capture(), org.mockito.ArgumentMatchers.eq(teamLead));
        assertEquals("Set up DB", captor.getValue().getTitle());
        assertEquals(1, captor.getValue().getProjectId());
    }

    @Test
    void showMenu_assignTicket() throws SQLException {
        // Arrange: "4", ticketId, userId, then "0"
        TicketController controller = controllerWithInput("4", "10", "4", "0");

        // Act
        controller.showMenu(teamLead);

        // Assert
        verify(ticketService, times(1)).assignTicket(10, 4, teamLead);
    }

    @Test
    void showMenu_advanceStatus() throws SQLException {
        // Arrange: "5", ticketId, newStatus, progress, comment, then "0"
        TicketController controller = controllerWithInput(
                "5", "10", "IN_PROGRESS", "20", "started work", "0");

        // Act
        controller.showMenu(teamLead);

        // Assert
        verify(ticketService, times(1)).advanceStatus(10, "IN_PROGRESS", 20, "started work", teamLead);
    }

    @Test
    void showMenu_deleteTicket() throws SQLException {
        // Arrange: "6", ticketId, then "0"
        TicketController controller = controllerWithInput("6", "10", "0");

        // Act
        controller.showMenu(teamLead);

        // Assert
        verify(ticketService, times(1)).deleteTicket(10, teamLead);
    }
}
