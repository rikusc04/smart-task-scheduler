package scheduler;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SchedulerEngineTest {
    private Task task(String id, double hours, int daysFromNow, Priority priority) {
        return new Task(id, "Task " + id, hours, LocalDate.now().plusDays(daysFromNow), priority);
    }

    @Test
    void emptyListProducesEmptySchedule() {
        Schedule s = SchedulerEngine.generateSchedule(List.of());
        assertTrue(s.getSlots().isEmpty());
        assertTrue(s.getUnschedulable().isEmpty());
    }

    @Test
    void singleTaskIsScheduled() {
        Schedule s = SchedulerEngine.generateSchedule(List.of(task("A", 2.0, 3, Priority.HIGH)));
        assertEquals(1, s.getSlots().size());
        assertTrue(s.getUnschedulable().isEmpty());
    }

    @Test
    void tasksDoNotExceedDailyHourCap() {
        // 5 tasks of 2h each = 10h, must spill into day 2
        List<Task> tasks = List.of(
            task("A", 2.0, 5, Priority.MEDIUM),
            task("B", 2.0, 5, Priority.MEDIUM),
            task("C", 2.0, 5, Priority.MEDIUM),
            task("D", 2.0, 5, Priority.MEDIUM),
            task("E", 2.0, 5, Priority.MEDIUM)
        );
        Schedule s = SchedulerEngine.generateSchedule(tasks);
        s.getSlots().forEach(slot -> {
            LocalDate day = slot.getStart().toLocalDate();
            assertTrue(s.getHoursUsed(day) <= SchedulerEngine.MAX_HOURS_PER_DAY,
                "Day " + day + " exceeded daily cap");
        });
    }

    @Test
    void highPriorityScheduledBeforeLowSameDeadline() {
        Task low  = new Task("L", "Low task",  1.0, LocalDate.now().plusDays(2), Priority.LOW);
        Task high = new Task("H", "High task", 1.0, LocalDate.now().plusDays(2), Priority.HIGH);
        Schedule s = SchedulerEngine.generateSchedule(List.of(low, high));

        assertEquals(2, s.getSlots().size());
        // HIGH should be the first slot of the day
        assertEquals("H", s.getSlots().get(0).getTask().getId());
    }

    @Test
    void taskMissedDeadlineMarkedUnschedulable() {
        // deadline is today but task takes 13h — won't fit in 12h cap
        Task t = new Task("X", "Too long", 13.0, LocalDate.now(), Priority.HIGH);
        Schedule s = SchedulerEngine.generateSchedule(List.of(t));
        assertEquals(1, s.getUnschedulable().size());
        assertTrue(s.getSlots().isEmpty());
    }

    @Test
    void noSlotsOverlap() {
        List<Task> tasks = List.of(
            task("A", 3.0, 4, Priority.HIGH),
            task("B", 3.0, 4, Priority.MEDIUM),
            task("C", 3.0, 4, Priority.LOW)
        );
        Schedule s = SchedulerEngine.generateSchedule(tasks);
        List<TimeSlot> slots = s.getSlots();
        for (int i = 0; i < slots.size(); i++) {
            for (int j = i + 1; j < slots.size(); j++) {
                assertFalse(slots.get(i).overlapsWith(slots.get(j)),
                    "Slots " + i + " and " + j + " overlap");
            }
        }
    }

    @Test
    void earliestDeadlineScheduledFirst() {
        Task later  = task("L", 1.0, 5, Priority.MEDIUM);
        Task sooner = task("S", 1.0, 2, Priority.MEDIUM);
        Schedule s = SchedulerEngine.generateSchedule(List.of(later, sooner));
        assertEquals("S", s.getSlots().get(0).getTask().getId());
    }
}
