package com.ptms.app.service;

import com.ptms.app.dao.TicketTrackingDao;
import com.ptms.app.exception.ResourceNotFoundException;
import com.ptms.app.model.TicketTracking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ITicketTrackingServiceTest {

    private TicketTrackingDao ticketTrackingDao;
    private TicketTrackingService ticketTrackingService;

    @BeforeEach
    void setUp() {
        ticketTrackingDao = mock(TicketTrackingDao.class);
        ticketTrackingService = new ITicketTrackingService(ticketTrackingDao);
    }

    @Test
    void getTrackingForTicket_ShouldThrowNotFoundWhenMissing() throws SQLException {
        // Arrange
        when(ticketTrackingDao.findByTicketId(99)).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> ticketTrackingService.getTrackingForTicket(99));
    }

    @Test
    void getTrackingForTicket() throws SQLException {
        // Arrange
        TicketTracking tracking = new TicketTracking(10, "IN_PROGRESS", 40, 4);
        when(ticketTrackingDao.findByTicketId(10)).thenReturn(tracking);

        // Act
        TicketTracking result = ticketTrackingService.getTrackingForTicket(10);

        // Assert
        assertEquals(40, result.getProgress());
    }

    @Test
    void getUpdatesByUser() throws SQLException {
        // Arrange
        TicketTracking tracking = new TicketTracking(10, "IN_PROGRESS", 40, 4);
        when(ticketTrackingDao.findByUpdatedBy(4)).thenReturn(List.of(tracking));

        // Act
        List<TicketTracking> result = ticketTrackingService.getUpdatesByUser(4);

        // Assert
        assertEquals(1, result.size());
    }
}
