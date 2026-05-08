package scheduler;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {
    private Task task(String id) {
        return new Task(id, "Task " + id, 1.0, LocalDate.now().plusDays(1), Priority.MEDIUM);
    }

    private TimeSlot slot(int startHour, int endHour) {
        LocalDateTime s = LocalDate.now().atTime(startHour, 0);
        LocalDateTime e = LocalDate.now().atTime(endHour, 0);
        return new TimeSlot(task("X"), s, e);
    }

    @Test
    void overlappingSlots() {
        TimeSlot a = slot(9, 11);
        TimeSlot b = slot(10, 12);
        assertTrue(a.overlapsWith(b));
        assertTrue(b.overlapsWith(a));
    }

    @Test
    void adjacentSlotsDoNotOverlap() {
        TimeSlot a = slot(9, 11);
        TimeSlot b = slot(11, 13);
        assertFalse(a.overlapsWith(b));
        assertFalse(b.overlapsWith(a));
    }

    @Test
    void nonOverlappingSlots() {
        TimeSlot a = slot(9, 10);
        TimeSlot b = slot(11, 12);
        assertFalse(a.overlapsWith(b));
    }

    @Test
    void slotOverlapsWithItself() {
        TimeSlot a = slot(9, 11);
        assertTrue(a.overlapsWith(a));
    }

    @Test
    void gettersReturnCorrectValues() {
        LocalDateTime start = LocalDate.now().atTime(9, 0);
        LocalDateTime end   = LocalDate.now().atTime(11, 0);
        Task t = task("A");
        TimeSlot slot = new TimeSlot(t, start, end);
        assertEquals(t, slot.getTask());
        assertEquals(start, slot.getStart());
        assertEquals(end, slot.getEnd());
    }
}
