package com.ptms.app.service;

import com.ptms.app.model.TicketTracking;

import java.sql.SQLException;
import java.util.List;

public interface TicketTrackingService {

    TicketTracking getTrackingForTicket(int ticketId) throws SQLException;

    /** Tracking rows most recently touched by this user — "what have I updated lately." */
    List<TicketTracking> getUpdatesByUser(int userId) throws SQLException;
}
