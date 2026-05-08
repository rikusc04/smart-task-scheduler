package scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SchedulerEngine {
    static final double MAX_HOURS_PER_DAY = 12.0;
    private static final int WORK_START_HOUR = 9;

    public static Schedule generateSchedule(List<Task> tasks) {
        Schedule schedule = new Schedule();

        // EDF primary sort, priority tiebreaker (HIGH before LOW)
        List<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator
            .comparing(Task::getDeadline)
            .thenComparing(Comparator.comparing(Task::getPriority).reversed()));

        LocalDate today = LocalDate.now();

        for (Task task : sorted) {
            boolean placed = false;
            LocalDate day = today;

            while (!day.isAfter(task.getDeadline())) {
                double used = schedule.getHoursUsed(day);
                if (used + task.getDuration() <= MAX_HOURS_PER_DAY) {
                    LocalDateTime start = day.atTime(WORK_START_HOUR, 0)
                        .plusMinutes((long) (used * 60));
                    LocalDateTime end = start.plusMinutes((long) (task.getDuration() * 60));
                    schedule.addSlot(new TimeSlot(task, start, end));
                    placed = true;
                    break;
                }
                day = day.plusDays(1);
            }

            if (!placed) {
                schedule.addUnschedulable(task);
            }
        }

        return schedule;
    }
}
