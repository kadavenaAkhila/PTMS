package com.ptms.app.controller;

import com.ptms.app.exception.ResourceNotFoundException;
import com.ptms.app.exception.UnauthorizedException;
import com.ptms.app.model.Client;
import com.ptms.app.model.User;
import com.ptms.app.service.ClientService;
import com.ptms.app.service.IClientService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientController {

    private static final Logger logger = Logger.getLogger(ClientController.class.getName());

    private final ClientService clientService;
    private final Scanner scanner;

    public ClientController() {
        this.clientService = new IClientService();
        this.scanner = new Scanner(System.in);
    }

    public ClientController(ClientService clientService, Scanner scanner) {
        this.clientService = clientService;
        this.scanner = scanner;
    }

    public void showMenu(User loggedInUser) {
        boolean running = true;
        while (running) {
            logger.info("\n--- Client Management ---");
            logger.info("1. Add client");
            logger.info("2. View all clients");
            logger.info("3. Search clients by name");
            logger.info("4. Update client");
            logger.info("5. Delete client");
            logger.info("0. Back");
            logger.info("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addClient(loggedInUser);
                    case "2" -> viewAllClients();
                    case "3" -> searchClients();
                    case "4" -> updateClient(loggedInUser);
                    case "5" -> deleteClient(loggedInUser);
                    case "0" -> running = false;
                    default -> logger.info("Invalid option, try again.");
                }
            } catch (UnauthorizedException | ResourceNotFoundException e) {
                logger.warning("Client action failed: " + e.getMessage());
                logger.info("Error: " + e.getMessage());
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Database error in ClientController", e);
                logger.info("Database error: " + e.getMessage());
            }
        }
    }

    private void addClient(User requestingUser) throws SQLException {
        logger.info("Client name: ");
        String name = scanner.nextLine().trim();
        logger.info("Email: ");
        String email = scanner.nextLine().trim();
        logger.info("Phone: ");
        String phone = scanner.nextLine().trim();
        logger.info("Company name: ");
        String companyName = scanner.nextLine().trim();

        Client client = new Client(name, email, phone, companyName);
        clientService.addClient(client, requestingUser);
        logger.info("Client added, id=" + client.getId());
    }

    private void viewAllClients() throws SQLException {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            logger.info("No clients found.");
            return;
        }
        clients.forEach(this::printClientSummary);
    }

    private void searchClients() throws SQLException {
        logger.info("Search keyword: ");
        String keyword = scanner.nextLine().trim();
        List<Client> results = clientService.searchClients(keyword);
        if (results.isEmpty()) {
            logger.info("No matching clients.");
            return;
        }
        results.forEach(this::printClientSummary);
    }

    private void updateClient(User requestingUser) throws SQLException {
        logger.info("Client id to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Client client = clientService.getClientById(id);

        logger.info("Leave a field blank to keep its current value.");

        logger.info("Name [" + client.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            client.setName(name);
        }

        logger.info("Email [" + client.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            client.setEmail(email);
        }

        logger.info("Phone [" + client.getPhone() + "]: ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) {
            client.setPhone(phone);
        }

        logger.info("Company name [" + client.getCompanyName() + "]: ");
        String companyName = scanner.nextLine().trim();
        if (!companyName.isEmpty()) {
            client.setCompanyName(companyName);
        }

        clientService.updateClient(client, requestingUser);
        logger.info("Client updated.");
    }

    private void deleteClient(User requestingUser) throws SQLException {
        logger.info("Client id to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        clientService.deleteClient(id, requestingUser);
        logger.info("Client deleted.");
    }

    private void printClientSummary(Client client) {
        logger.info(String.format("id=%d | %s | email=%s | phone=%s | company=%s",
                client.getId(), client.getName(), client.getEmail(), client.getPhone(), client.getCompanyName()));
    }
}