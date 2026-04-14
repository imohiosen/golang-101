package cron;

import java.time.*;
import java.util.*;

/**
 * Calculates the next N execution times for a given Schedule from a start time.
 */
public class NextTime {

    /**
     * Find the next N execution times starting from 'from'.
     */
    public static List<LocalDateTime> nextN(Schedule schedule, LocalDateTime from, int count) {
        List<LocalDateTime> results = new ArrayList<>();
        LocalDateTime current = from.plusMinutes(1).withSecond(0).withNano(0); // start from next minute

        while (results.size() < count) {
            if (matches(schedule, current)) {
                results.add(current);
            }
            current = current.plusMinutes(1);

            // Safety: don't loop forever (max 2 years ahead)
            if (current.isAfter(from.plusYears(2))) {
                break;
            }
        }

        return results;
    }

    /**
     * Check if a given time matches the schedule.
     */
    public static boolean matches(Schedule schedule, LocalDateTime time) {
        return schedule.getMinutes().contains(time.getMinute())
                && schedule.getHours().contains(time.getHour())
                && schedule.getDaysOfMonth().contains(time.getDayOfMonth())
                && schedule.getMonths().contains(time.getMonthValue())
                && schedule.getDaysOfWeek().contains(time.getDayOfWeek().getValue() % 7);
    }
}
