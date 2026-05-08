package scheduler;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class MainApp {
    private static final List<Task> tasks = new ArrayList<>();
    private static Schedule currentSchedule = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printHelp();

        while (true) {
            System.out.print("\n> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            switch (line.split("\\s+")[0].toLowerCase()) {
                case "add"      -> handleAdd(scanner);
                case "list"     -> handleList();
                case "delete"   -> handleDelete(scanner);
                case "schedule" -> handleSchedule();
                case "suggest"  -> handleSuggest();
                case "help"     -> printHelp();
                case "quit", "exit" -> { System.out.println("Goodbye."); return; }
                default -> System.out.println("Unknown command. Type 'help' for options.");
            }
        }
    }

    private static void handleAdd(Scanner scanner) {
        try {
            System.out.print("Task name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Duration (hours): ");
            double duration = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Deadline (YYYY-MM-DD): ");
            LocalDate deadline = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Priority (LOW/MEDIUM/HIGH): ");
            Priority priority = Priority.fromString(scanner.nextLine().trim());

            ConstraintValidator.validate(name, duration, deadline, priority);

            String id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            Task task = new Task(id, name, duration, deadline, priority);
            tasks.add(task);
            currentSchedule = null; // invalidate cached schedule
            System.out.println("Added: " + task);

        } catch (NumberFormatException e) {
            System.out.println("Error: duration must be a number.");
        } catch (DateTimeParseException e) {
            System.out.println("Error: invalid date format. Use YYYY-MM-DD.");
        } catch (InvalidTaskException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleList() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
            return;
        }
        tasks.forEach(t -> System.out.println("  " + t));
    }

    private static void handleDelete(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to delete.");
            return;
        }
        handleList();
        System.out.print("Task ID to delete: ");
        String id = scanner.nextLine().trim().toUpperCase();
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));
        if (removed) {
            currentSchedule = null;
            System.out.println("Deleted task " + id + ".");
        } else {
            System.out.println("No task with ID: " + id);
        }
    }

    private static void handleSchedule() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to schedule.");
            return;
        }
        currentSchedule = SchedulerEngine.generateSchedule(tasks);
        currentSchedule.display();
    }

    private static void handleSuggest() {
        if (currentSchedule == null) {
            System.out.println("Run 'schedule' first to generate a schedule.");
            return;
        }
        System.out.println("Consulting AI...");
        System.out.println(AiAdvisor.getSuggestions(currentSchedule));
    }

    private static void printHelp() {
        System.out.println("Smart Task Scheduler");
        System.out.println("  add      - Add a new task");
        System.out.println("  list     - List all tasks");
        System.out.println("  delete   - Delete a task by ID");
        System.out.println("  schedule - Generate and display the schedule");
        System.out.println("  suggest  - Get AI suggestions (requires ANTHROPIC_API_KEY)");
        System.out.println("  help     - Show this menu");
        System.out.println("  quit     - Exit");
    }
}
