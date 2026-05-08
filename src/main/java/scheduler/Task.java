package scheduler;

import java.time.LocalDate;

public class Task {
    private final String id;
    private String name;
    private double duration; // hours
    private LocalDate deadline;
    private Priority priority;

    public Task(String id, String name, double duration, LocalDate deadline, Priority priority) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getId()           { return id; }
    public String getName()         { return name; }
    public double getDuration()     { return duration; }
    public LocalDate getDeadline()  { return deadline; }
    public Priority getPriority()   { return priority; }

    public void setName(String name)          { this.name = name; }
    public void setDuration(double duration)  { this.duration = duration; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public void setPriority(Priority priority)  { this.priority = priority; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%.1fh, due %s, %s)", id, name, duration, deadline, priority);
    }
}
