package com.ptms.app.service;

import com.ptms.app.model.Ticket;
import com.ptms.app.model.User;

import java.sql.SQLException;
import java.util.List;

public interface TicketService {

    Ticket createTicket(Ticket ticket, User requestingUser) throws SQLException;

    Ticket getTicketById(int id) throws SQLException;

    List<Ticket> getTicketsForProject(int projectId) throws SQLException;

    List<Ticket> getTicketsForUser(int userId) throws SQLException;

    void assignTicket(int ticketId, int userId, User requestingUser) throws SQLException;

    /**
     * Moves a ticket to a new status, enforcing the workflow:
     * IN_DEVELOPMENT -> IN_PROGRESS -> IMPLEMENTED -> COMPLETED (approve)
     * IMPLEMENTED -> IN_PROGRESS (sent back for rework)
     * Also keeps ticket_tracking's status/progress in sync.
     */
    void advanceStatus(int ticketId, String newStatus, int progress, String comment, User requestingUser) throws SQLException;

    void deleteTicket(int ticketId, User requestingUser) throws SQLException;
}