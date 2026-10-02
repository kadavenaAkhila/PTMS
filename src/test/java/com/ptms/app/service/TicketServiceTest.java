package com.ptms.app.service;

import com.ptms.app.dao.TicketDao;
import com.ptms.app.dao.TicketTrackingDao;
import com.ptms.app.exception.UnauthorizedException;
import com.ptms.app.exception.ValidationException;
import com.ptms.app.model.Ticket;
import com.ptms.app.model.TicketTracking;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ITicketServiceTest {

    private TicketDao ticketDao;
    private TicketTrackingDao ticketTrackingDao;
    private TicketService ticketService;

    private User teamMember;
    private User teamLead;

    @BeforeEach
    void setUp() {
        ticketDao = mock(TicketDao.class);
        ticketTrackingDao = mock(TicketTrackingDao.class);
        ticketService = new ITicketService(ticketDao, ticketTrackingDao);

        teamMember = new User("Eli", "Emp", "eemp", "e@ptms.com", "hash", User.Role.TEAM_MEMBER);
        teamMember.setId(4);

        teamLead = new User("Tara", "Lead", "tlead", "t@ptms.com", "hash", User.Role.TEAM_LEAD);
        teamLead.setId(3);
    }

    private Ticket ticketWithStatus(String status) {
        Ticket ticket = new Ticket(1, "Set up DB", "desc", "HIGH");
        ticket.setId(10);
        ticket.setAssignedTo(4);
        ticket.setStatus(status);
        return ticket;
    }

    @Test
    void advanceStatus() throws SQLException {
        // Arrange
        Ticket ticket = ticketWithStatus("IN_DEVELOPMENT");
        when(ticketDao.findById(10)).thenReturn(ticket);
        lenient().when(ticketTrackingDao.findByTicketId(10))
                .thenReturn(new TicketTracking(10, "IN_DEVELOPMENT", 0, 4));

        // Act
        ticketService.advanceStatus(10, "IN_PROGRESS", 20, "started work", teamMember);

        // Assert
        assertEquals("IN_PROGRESS", ticket.getStatus());
    }

    @Test
    void advanceStatus_ShouldThrowValidationExceptionForSkippedState() throws SQLException {
        // Arrange
        Ticket ticket = ticketWithStatus("IN_DEVELOPMENT");
        when(ticketDao.findById(10)).thenReturn(ticket);

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> ticketService.advanceStatus(10, "COMPLETED", 100, "done", teamMember));
    }

    @Test
    void advanceStatus_ShouldThrowUnauthorizedExceptionForAssigneeApprovingOwnTicket() throws SQLException {
        // Arrange
        Ticket ticket = ticketWithStatus("IMPLEMENTED");
        when(ticketDao.findById(10)).thenReturn(ticket);

        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> ticketService.advanceStatus(10, "COMPLETED", 100, "looks done", teamMember));
    }

    @Test
    void advanceStatus_ShouldAllowLeadToApprove() throws SQLException {
        // Arrange
        Ticket ticket = ticketWithStatus("IMPLEMENTED");
        when(ticketDao.findById(10)).thenReturn(ticket);
        when(ticketTrackingDao.findByTicketId(10))
                .thenReturn(new TicketTracking(10, "IMPLEMENTED", 90, 4));

        // Act
        ticketService.advanceStatus(10, "COMPLETED", 90, "approved", teamLead);

        // Assert
        assertEquals("COMPLETED", ticket.getStatus());
    }

    @Test
    void advanceStatus_ShouldForceProgressTo100OnCompletion() throws SQLException {
        // Arrange
        Ticket ticket = ticketWithStatus("IMPLEMENTED");
        TicketTracking tracking = new TicketTracking(10, "IMPLEMENTED", 90, 4);
        when(ticketDao.findById(10)).thenReturn(ticket);
        when(ticketTrackingDao.findByTicketId(10)).thenReturn(tracking);

        // Act
        ticketService.advanceStatus(10, "COMPLETED", 50, "approved", teamLead);

        // Assert
        assertEquals(100, tracking.getProgress());
    }

    @Test
    void advanceStatus_ShouldAllowLeadToSendBackForRework() throws SQLException {
        // Arrange
        Ticket ticket = ticketWithStatus("IMPLEMENTED");
        when(ticketDao.findById(10)).thenReturn(ticket);
        when(ticketTrackingDao.findByTicketId(10))
                .thenReturn(new TicketTracking(10, "IMPLEMENTED", 90, 4));

        // Act
        ticketService.advanceStatus(10, "IN_PROGRESS", 70, "needs more work", teamLead);

        // Assert
        assertEquals("IN_PROGRESS", ticket.getStatus());
    }
}