package com.ptms.app.controller;

import com.ptms.app.exception.ResourceNotFoundException;
import com.ptms.app.exception.UnauthorizedException;
import com.ptms.app.exception.ValidationException;
import com.ptms.app.model.Ticket;
import com.ptms.app.model.User;
import com.ptms.app.service.ITicketService;
import com.ptms.app.service.TicketService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TicketController {

    private final TicketService ticketService;
    private final Scanner scanner;

    public TicketController() {
        this.ticketService = new ITicketService();
        this.scanner = new Scanner(System.in);
    }

    public TicketController(TicketService ticketService, Scanner scanner) {
        this.ticketService = ticketService;
        this.scanner = scanner;
    }

    public void showMenu(User loggedInUser) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Ticket Management ---");
            System.out.println("1. Create ticket");
            System.out.println("2. View tickets for a project");
            System.out.println("3. View my assigned tickets");
            System.out.println("4. Assign ticket");
            System.out.println("5. Advance ticket status");
            System.out.println("6. Delete ticket");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> createTicket(loggedInUser);
                    case "2" -> viewTicketsForProject();
                    case "3" -> viewMyTickets(loggedInUser);
                    case "4" -> assignTicket(loggedInUser);
                    case "5" -> advanceStatus(loggedInUser);
                    case "6" -> deleteTicket(loggedInUser);
                    case "0" -> running = false;
                    default -> System.out.println("Invalid option, try again.");
                }
            } catch (UnauthorizedException | ValidationException | ResourceNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private void createTicket(User requestingUser) throws SQLException {
        System.out.print("Project id: ");
        int projectId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        System.out.print("Priority (LOW, MEDIUM, HIGH): ");
        String priority = scanner.nextLine().trim().toUpperCase();
        System.out.print("Deadline (YYYY-MM-DD, blank to skip): ");
        String deadlineInput = scanner.nextLine().trim();
        LocalDate deadline = deadlineInput.isEmpty() ? null : LocalDate.parse(deadlineInput);

        Ticket ticket = new Ticket(projectId, title, description, priority);
        ticket.setDeadline(deadline);

        ticketService.createTicket(ticket, requestingUser);
        System.out.println("Ticket created, id=" + ticket.getId());
    }

    private void viewTicketsForProject() throws SQLException {
        System.out.print("Project id: ");
        int projectId = Integer.parseInt(scanner.nextLine().trim());
        List<Ticket> tickets = ticketService.getTicketsForProject(projectId);
        if (tickets.isEmpty()) {
            System.out.println("No tickets found for this project.");
            return;
        }
        tickets.forEach(this::printTicketSummary);
    }

    private void viewMyTickets(User requestingUser) throws SQLException {
        List<Ticket> tickets = ticketService.getTicketsForUser(requestingUser.getId());
        if (tickets.isEmpty()) {
            System.out.println("No tickets assigned to you.");
            return;
        }
        tickets.forEach(this::printTicketSummary);
    }

    private void assignTicket(User requestingUser) throws SQLException {
        System.out.print("Ticket id: ");
        int ticketId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("User id to assign: ");
        int userId = Integer.parseInt(scanner.nextLine().trim());

        ticketService.assignTicket(ticketId, userId, requestingUser);
        System.out.println("Ticket assigned.");
    }

    private void advanceStatus(User requestingUser) throws SQLException {
        System.out.print("Ticket id: ");
        int ticketId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("New status (IN_PROGRESS, IMPLEMENTED, COMPLETED): ");
        String newStatus = scanner.nextLine().trim().toUpperCase();
        System.out.print("Progress (0-100): ");
        int progress = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Comment: ");
        String comment = scanner.nextLine().trim();

        ticketService.advanceStatus(ticketId, newStatus, progress, comment, requestingUser);
        System.out.println("Ticket status updated to " + newStatus + ".");
    }

    private void deleteTicket(User requestingUser) throws SQLException {
        System.out.print("Ticket id to delete: ");
        int ticketId = Integer.parseInt(scanner.nextLine().trim());
        ticketService.deleteTicket(ticketId, requestingUser);
        System.out.println("Ticket deleted.");
    }

    private void printTicketSummary(Ticket ticket) {
        System.out.printf("id=%d | %s | project=%d | priority=%s | status=%s | assignedTo=%s%n",
                ticket.getId(), ticket.getTitle(), ticket.getProjectId(),
                ticket.getPriority(), ticket.getStatus(), ticket.getAssignedTo());
    }
}
