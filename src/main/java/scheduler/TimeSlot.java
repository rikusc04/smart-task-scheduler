package scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeSlot {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final Task task;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public TimeSlot(Task task, LocalDateTime start, LocalDateTime end) {
        this.task = task;
        this.start = start;
        this.end = end;
    }

    public Task getTask()           { return task; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd()   { return end; }

    public boolean overlapsWith(TimeSlot other) {
        return this.start.isBefore(other.end) && other.start.isBefore(this.end);
    }

    @Override
    public String toString() {
        return String.format("%s-%s  %s [%s]",
            start.format(FMT), end.format(FMT), task.getName(), task.getPriority());
    }
}
