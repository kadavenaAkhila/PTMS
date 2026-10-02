package com.ptms.app.controller;

import com.ptms.app.exception.ResourceNotFoundException;
import com.ptms.app.model.TicketTracking;
import com.ptms.app.model.User;
import com.ptms.app.service.ITicketTrackingService;
import com.ptms.app.service.TicketTrackingService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Read-only by design — see ITicketTrackingService for why writes go
 * through TicketController's "advance status" action instead of here.
 */
public class TicketTrackingController {

    private static final Logger logger = Logger.getLogger(TicketTrackingController.class.getName());

    private final TicketTrackingService ticketTrackingService;
    private final Scanner scanner;

    public TicketTrackingController() {
        this.ticketTrackingService = new ITicketTrackingService();
        this.scanner = new Scanner(System.in);
    }

    public TicketTrackingController(TicketTrackingService ticketTrackingService, Scanner scanner) {
        this.ticketTrackingService = ticketTrackingService;
        this.scanner = scanner;
    }

    public void showMenu(User loggedInUser) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Ticket Tracking (view only) ---");
            System.out.println("1. View tracking for a ticket");
            System.out.println("2. View my recent updates");
            System.out.println("0. Back");
            System.out.println("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> viewTrackingForTicket();
                    case "2" -> viewMyUpdates(loggedInUser);
                    case "0" -> running = false;
                    default -> System.out.println("Invalid option, try again.");
                }
            } catch (ResourceNotFoundException e) {
                logger.warning("Ticket tracking lookup failed: " + e.getMessage());
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Database error in TicketTrackingController", e);
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private void viewTrackingForTicket() throws SQLException {
        System.out.println("Ticket id: ");
        int ticketId = Integer.parseInt(scanner.nextLine().trim());
        TicketTracking tracking = ticketTrackingService.getTrackingForTicket(ticketId);
        printTrackingSummary(tracking);
    }

    private void viewMyUpdates(User requestingUser) throws SQLException {
        List<TicketTracking> updates = ticketTrackingService.getUpdatesByUser(requestingUser.getId());
        if (updates.isEmpty()) {
            System.out.println("You haven't updated any tickets yet.");
            return;
        }
        updates.forEach(this::printTrackingSummary);
    }

    private void printTrackingSummary(TicketTracking tracking) {
        System.out.println(String.format("ticketId=%d | status=%s | progress=%d%% | updatedBy=%s | updatedAt=%s | comment=%s",
                tracking.getTicketId(), tracking.getStatus(), tracking.getProgress(),
                tracking.getUpdatedBy(), tracking.getUpdatedAt(), tracking.getComment()));
    }
}