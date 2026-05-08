package scheduler;

import java.time.LocalDate;

public class ConstraintValidator {

    public static void validate(String name, double duration, LocalDate deadline, Priority priority) {
        if (name == null || name.isBlank()) {
            throw new InvalidTaskException("Task name cannot be empty.");
        }
        if (duration <= 0) {
            throw new InvalidTaskException("Duration must be positive, got: " + duration);
        }
        if (duration > 12) {
            throw new InvalidTaskException(
                "Duration cannot exceed 12 hours (got: " + duration + "h). Please get some sleep.");
        }
        if (deadline == null) {
            throw new InvalidTaskException("Deadline cannot be null.");
        }
        if (deadline.isBefore(LocalDate.now())) {
            throw new InvalidTaskException("Deadline cannot be in the past: " + deadline);
        }
        if (priority == null) {
            throw new InvalidTaskException("Priority cannot be null.");
        }
    }
}
