package scheduler;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task makeTask() {
        return new Task("T1", "Write report", 2.0, LocalDate.now().plusDays(3), Priority.HIGH);
    }

    @Test
    void gettersReturnConstructorValues() {
        Task t = makeTask();
        assertEquals("T1", t.getId());
        assertEquals("Write report", t.getName());
        assertEquals(2.0, t.getDuration());
        assertEquals(Priority.HIGH, t.getPriority());
    }

    @Test
    void settersUpdateValues() {
        Task t = makeTask();
        t.setName("Updated");
        t.setDuration(3.5);
        t.setPriority(Priority.LOW);
        assertEquals("Updated", t.getName());
        assertEquals(3.5, t.getDuration());
        assertEquals(Priority.LOW, t.getPriority());
    }

    @Test
    void toStringContainsKeyFields() {
        Task t = makeTask();
        String s = t.toString();
        assertTrue(s.contains("T1"));
        assertTrue(s.contains("Write report"));
        assertTrue(s.contains("HIGH"));
    }
}
