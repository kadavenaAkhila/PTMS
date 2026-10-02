package com.ptms.app.controller;

import com.ptms.app.model.TicketTracking;
import com.ptms.app.model.User;
import com.ptms.app.service.TicketTrackingService;
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
class TicketTrackingControllerTest {

    private TicketTrackingService ticketTrackingService;
    private User teamMember;

    @BeforeEach
    void setUp() {
        ticketTrackingService = mock(TicketTrackingService.class);

        teamMember = new User("Eli", "Emp", "eemp", "e@ptms.com", "hash", User.Role.TEAM_MEMBER);
        teamMember.setId(4);
    }

    private TicketTrackingController controllerWithInput(String... lines) {
        String simulatedInput = String.join("\n", lines) + "\n";
        return new TicketTrackingController(ticketTrackingService, new Scanner(simulatedInput));
    }

    @Test
    void showMenu_viewTrackingForTicket() throws SQLException {
        // Arrange: "1", ticketId, then "0"
        when(ticketTrackingService.getTrackingForTicket(10))
                .thenReturn(new TicketTracking(10, "IN_PROGRESS", 40, 4));
        TicketTrackingController controller = controllerWithInput("1", "10", "0");

        // Act
        controller.showMenu(teamMember);

        // Assert
        verify(ticketTrackingService, times(1)).getTrackingForTicket(10);
    }

    @Test
    void showMenu_viewMyUpdates() throws SQLException {
        // Arrange: "2", then "0"
        when(ticketTrackingService.getUpdatesByUser(4)).thenReturn(java.util.List.of());
        TicketTrackingController controller = controllerWithInput("2", "0");

        // Act
        controller.showMenu(teamMember);

        // Assert
        verify(ticketTrackingService, times(1)).getUpdatesByUser(4);
    }
}
