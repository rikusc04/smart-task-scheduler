package scheduler;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ConstraintValidatorTest {
    private final LocalDate future = LocalDate.now().plusDays(5);

    @Test
    void validInputPasses() {
        assertDoesNotThrow(() ->
            ConstraintValidator.validate("Task A", 2.0, future, Priority.HIGH));
    }

    @Test
    void emptyNameThrows() {
        assertThrows(InvalidTaskException.class, () ->
            ConstraintValidator.validate("", 2.0, future, Priority.LOW));
    }

    @Test
    void blankNameThrows() {
        assertThrows(InvalidTaskException.class, () ->
            ConstraintValidator.validate("   ", 2.0, future, Priority.LOW));
    }

    @Test
    void negativeDurationThrows() {
        assertThrows(InvalidTaskException.class, () ->
            ConstraintValidator.validate("Task", -1.0, future, Priority.LOW));
    }

    @Test
    void zeroDurationThrows() {
        assertThrows(InvalidTaskException.class, () ->
            ConstraintValidator.validate("Task", 0.0, future, Priority.LOW));
    }

    @Test
    void durationOver12Throws() {
        assertThrows(InvalidTaskException.class, () ->
            ConstraintValidator.validate("Task", 13.0, future, Priority.LOW));
    }

    @Test
    void pastDeadlineThrows() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        assertThrows(InvalidTaskException.class, () ->
            ConstraintValidator.validate("Task", 1.0, yesterday, Priority.LOW));
    }

    @Test
    void nullDeadlineThrows() {
        assertThrows(InvalidTaskException.class, () ->
            ConstraintValidator.validate("Task", 1.0, null, Priority.LOW));
    }

    @Test
    void nullPriorityThrows() {
        assertThrows(InvalidTaskException.class, () ->
            ConstraintValidator.validate("Task", 1.0, future, null));
    }
}
