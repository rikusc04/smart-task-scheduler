package scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Schedule {
    private final List<TimeSlot> slots = new ArrayList<>();
    private final List<Task> unschedulable = new ArrayList<>();
    private final Map<LocalDate, Double> hoursUsed = new LinkedHashMap<>();

    public void addSlot(TimeSlot slot) {
        slots.add(slot);
        LocalDate day = slot.getStart().toLocalDate();
        hoursUsed.merge(day, slot.getTask().getDuration(), Double::sum);
    }

    public void addUnschedulable(Task task) {
        unschedulable.add(task);
    }

    public double getHoursUsed(LocalDate day) {
        return hoursUsed.getOrDefault(day, 0.0);
    }

    public List<TimeSlot> getSlots()          { return slots; }
    public List<Task> getUnschedulable()      { return unschedulable; }
    public Map<LocalDate, Double> getHoursUsedMap() { return hoursUsed; }

    public void display() {
        if (slots.isEmpty()) {
            System.out.println("No tasks scheduled.");
        } else {
            Map<LocalDate, List<TimeSlot>> byDay = groupByDay();
            for (Map.Entry<LocalDate, List<TimeSlot>> entry : byDay.entrySet()) {
                System.out.println("\n--- " + entry.getKey() + " ---");
                for (TimeSlot slot : entry.getValue()) {
                    System.out.println("  " + slot);
                }
                System.out.printf("  Total: %.1f hours%n", hoursUsed.get(entry.getKey()));
            }
        }

        if (!unschedulable.isEmpty()) {
            System.out.println("\n[!] Could not schedule before deadline:");
            for (Task t : unschedulable) {
                System.out.println("  " + t);
            }
        }
    }

    public String toPromptString() {
        if (slots.isEmpty()) return "No tasks scheduled.";

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LocalDate, List<TimeSlot>> entry : groupByDay().entrySet()) {
            sb.append(entry.getKey()).append(": ");
            for (TimeSlot slot : entry.getValue()) {
                sb.append(String.format("[%s, %.1fh, %s] ",
                    slot.getTask().getName(), slot.getTask().getDuration(), slot.getTask().getPriority()));
            }
            sb.append(String.format("(%.1fh total)\n", hoursUsed.get(entry.getKey())));
        }

        if (!unschedulable.isEmpty()) {
            sb.append("Could not schedule: ");
            unschedulable.forEach(t -> sb.append(t.getName()).append(" "));
        }

        return sb.toString();
    }

    private Map<LocalDate, List<TimeSlot>> groupByDay() {
        Map<LocalDate, List<TimeSlot>> byDay = new LinkedHashMap<>();
        for (TimeSlot slot : slots) {
            byDay.computeIfAbsent(slot.getStart().toLocalDate(), k -> new ArrayList<>()).add(slot);
        }
        return byDay;
    }
}
