package cron;

import java.util.*;

/**
 * Represents a parsed cron schedule with expanded field values.
 * Each field holds the set of valid values (e.g., minutes 0,15,30,45).
 */
public class Schedule {

    private final Set<Integer> minutes;    // 0-59
    private final Set<Integer> hours;      // 0-23
    private final Set<Integer> daysOfMonth;// 1-31
    private final Set<Integer> months;     // 1-12
    private final Set<Integer> daysOfWeek; // 0-6 (Sunday=0)
    private final String original;

    public Schedule(String original, Set<Integer> minutes, Set<Integer> hours,
                    Set<Integer> daysOfMonth, Set<Integer> months, Set<Integer> daysOfWeek) {
        this.original = original;
        this.minutes = minutes;
        this.hours = hours;
        this.daysOfMonth = daysOfMonth;
        this.months = months;
        this.daysOfWeek = daysOfWeek;
    }

    public Set<Integer> getMinutes() { return minutes; }
    public Set<Integer> getHours() { return hours; }
    public Set<Integer> getDaysOfMonth() { return daysOfMonth; }
    public Set<Integer> getMonths() { return months; }
    public Set<Integer> getDaysOfWeek() { return daysOfWeek; }
    public String getOriginal() { return original; }

    /**
     * Pretty-print the expanded schedule in a table format.
     */
    public String prettyPrint() {
        return String.format(
                "%-14s %s%n%-14s %s%n%-14s %s%n%-14s %s%n%-14s %s",
                "minute", formatSet(minutes),
                "hour", formatSet(hours),
                "day of month", formatSet(daysOfMonth),
                "month", formatSet(months),
                "day of week", formatSet(daysOfWeek)
        );
    }

    private String formatSet(Set<Integer> set) {
        return new TreeSet<>(set).toString()
                .replace("[", "").replace("]", "");
    }
}
