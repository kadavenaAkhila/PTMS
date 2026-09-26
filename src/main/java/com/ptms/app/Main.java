package com.ptms.app;

import com.ptms.app.controller.ClientController;
import com.ptms.app.controller.ProjectController;
import com.ptms.app.controller.ProjectMemberController;
import com.ptms.app.controller.TicketController;
import com.ptms.app.controller.TicketTrackingController;
import com.ptms.app.controller.UserController;
import com.ptms.app.model.User;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * The one entry point that actually runs the app.
 * Everything else (model/dao/service/controller) is just classes sitting
 * there until something like this calls them.
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserController userController = new UserController();

        User loggedInUser = null;

        while (loggedInUser == null) {
            logger.info("\n=== PTMS ===");
            logger.info("1. Login");
            logger.info("2. Register");
            logger.info("0. Exit");
            logger.info("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> loggedInUser = userController.login();
                    case "2" -> loggedInUser = userController.registerUser();
                    case "0" -> {
                        logger.info("Goodbye.");
                        return;
                    }
                    default -> logger.info("Invalid option, try again.");
                }
            } catch (SQLException e) {
                logger.severe("Database error: " + e.getMessage());
                logger.info("Check DBConnection / db.properties and that MySQL is running.");
            }
        }

        // Once logged in, hand off to a main dashboard offering each area.
        showDashboard(loggedInUser, userController, scanner);
    }

    private static void showDashboard(User loggedInUser, UserController userController, Scanner scanner) {
        ClientController clientController = new ClientController();
        ProjectController projectController = new ProjectController();
        ProjectMemberController projectMemberController = new ProjectMemberController();
        TicketController ticketController = new TicketController();
        TicketTrackingController ticketTrackingController = new TicketTrackingController();
        boolean running = true;

        while (running) {
            logger.info("\n=== Dashboard (" + loggedInUser.getUsername() + " / " + loggedInUser.getRole() + ") ===");
            logger.info("1. User Management");
            logger.info("2. Client Management");
            logger.info("3. Project Management");
            logger.info("4. Project Members");
            logger.info("5. Ticket Management");
            logger.info("6. Ticket Tracking (view only)");
            logger.info("0. Logout");
            logger.info("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> userController.showMenu(loggedInUser);
                case "2" -> clientController.showMenu(loggedInUser);
                case "3" -> projectController.showMenu(loggedInUser);
                case "4" -> projectMemberController.showMenu(loggedInUser);
                case "5" -> ticketController.showMenu(loggedInUser);
                case "6" -> ticketTrackingController.showMenu(loggedInUser);
                case "0" -> {
                    logger.info("Logged out.");
                    running = false;
                }
                default -> logger.info("Invalid option, try again.");
            }
        }
    }
}