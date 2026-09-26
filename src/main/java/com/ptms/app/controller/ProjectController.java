package com.ptms.app.controller;
import com.ptms.app.exception.ResourceNotFoundException;
import com.ptms.app.exception.UnauthorizedException;
import com.ptms.app.exception.ValidationException;
import com.ptms.app.model.Project;
import com.ptms.app.model.User;
import com.ptms.app.service.IProjectService;
import com.ptms.app.service.ProjectService;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ProjectController {
    private static final Logger logger = Logger.getLogger(ProjectController.class.getName());
    private final ProjectService projectService;
    private final Scanner scanner;
    public ProjectController() {
        this.projectService = new IProjectService();
        this.scanner = new Scanner(System.in);
    }
    public ProjectController(ProjectService projectService, Scanner scanner) {
        this.projectService = projectService;
        this.scanner = scanner;
    }
    public void showMenu(User loggedInUser) {
        boolean running = true;
        while (running) {
            logger.info("\n--- Project Management ---");
            logger.info("1. Create project");
            logger.info("2. View my projects");
            logger.info("3. View all projects");
            logger.info("4. View project details");
            logger.info("5. Assign team lead");
            logger.info("6. Update project");
            logger.info("7. Delete project");
            logger.info("0. Back");
            logger.info("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> createProject(loggedInUser);
                    case "2" -> viewMyProjects(loggedInUser);
                    case "3" -> viewAllProjects();
                    case "4" -> viewProjectDetails();
                    case "5" -> assignTeamLead(loggedInUser);
                    case "6" -> updateProject(loggedInUser);
                    case "7" -> deleteProject(loggedInUser);
                    case "0" -> running = false;
                    default -> logger.info("Invalid option, try again.");
                }
            } catch (UnauthorizedException | ValidationException | ResourceNotFoundException e) {
                logger.warning("Project action failed: " + e.getMessage());
                logger.info("Error: " + e.getMessage());
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Database error in ProjectController", e);
                logger.info("Database error: " + e.getMessage());
            }
        }
    }

    private void createProject(User requestingUser) throws SQLException {
        logger.info("Project name: ");
        String name = scanner.nextLine().trim();
        logger.info("Requirements: ");
        String requirements = scanner.nextLine().trim();
        logger.info("Domain: ");
        String domain = scanner.nextLine().trim();
        logger.info("Cost: ");
        BigDecimal cost = parseBigDecimalOrNull(scanner.nextLine().trim());
        logger.info("Start date (YYYY-MM-DD, blank to skip): ");
        LocalDate startDate = parseDateOrNull(scanner.nextLine().trim());
        logger.info("Deadline (YYYY-MM-DD, blank to skip): ");
        LocalDate deadline = parseDateOrNull(scanner.nextLine().trim());
        logger.info("Priority (LOW, MEDIUM, HIGH): ");
        String priority = scanner.nextLine().trim().toUpperCase();

        Project project = new Project(name, requirements, requestingUser.getId(), priority);
        project.setDomain(domain);
        project.setCost(cost);
        project.setStartDate(startDate);
        project.setDeadline(deadline);

        projectService.createProject(project, requestingUser);
        logger.info("Project created, id=" + project.getId());
    }

    private void viewMyProjects(User requestingUser) throws SQLException {
        List<Project> projects = projectService.getProjectsForUser(requestingUser);
        if (projects.isEmpty()) {
            logger.info("No projects found for you.");
            return;
        }
        projects.forEach(this::printProjectSummary);
    }

    private void viewAllProjects() throws SQLException {
        List<Project> projects = projectService.getAllProjects();
        if (projects.isEmpty()) {
            logger.info("No projects found.");
            return;
        }
        projects.forEach(this::printProjectSummary);
    }

    private void viewProjectDetails() throws SQLException {
        logger.info("Project id: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Project project = projectService.getProjectById(id);
        logger.info(project.toString());
    }

    private void assignTeamLead(User requestingUser) throws SQLException {
        logger.info("Project id: ");
        int projectId = Integer.parseInt(scanner.nextLine().trim());
        logger.info("Team lead user id: ");
        int teamLeadId = Integer.parseInt(scanner.nextLine().trim());

        projectService.assignTeamLead(projectId, teamLeadId, requestingUser);
        logger.info("Team lead assigned.");
    }

    private void updateProject(User requestingUser) throws SQLException {
        logger.info("Project id to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Project project = projectService.getProjectById(id);

        logger.info("Leave a field blank to keep its current value.");

        logger.info("Name [" + project.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            project.setName(name);
        }

        logger.info("Status [" + project.getStatus() + "]: ");
        String status = scanner.nextLine().trim();
        if (!status.isEmpty()) {
            project.setStatus(status);
        }

        logger.info("Priority [" + project.getPriority() + "]: ");
        String priority = scanner.nextLine().trim();
        if (!priority.isEmpty()) {
            project.setPriority(priority.toUpperCase());
        }

        projectService.updateProject(project, requestingUser);
        logger.info("Project updated.");
    }

    private void deleteProject(User requestingUser) throws SQLException {
        logger.info("Project id to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        projectService.deleteProject(id, requestingUser);
        logger.info("Project deleted.");
    }

    private BigDecimal parseBigDecimalOrNull(String input) {
        if (input.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            logger.info("Not a valid number, leaving cost blank.");
            return null;
        }
    }

    private LocalDate parseDateOrNull(String input) {
        if (input.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(input);
        } catch (Exception e) {
            logger.info("Not a valid date (expected YYYY-MM-DD), leaving blank.");
            return null;
        }
    }

    private void printProjectSummary(Project project) {
        logger.info(String.format("id=%d | %s | manager=%d | teamLead=%s | status=%s | priority=%s",
                project.getId(), project.getName(), project.getManagerId(),
                project.getTeamLeadId(), project.getStatus(), project.getPriority()));
    }
}