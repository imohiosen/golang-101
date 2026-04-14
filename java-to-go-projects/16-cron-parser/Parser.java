package cron;

import java.util.*;

/**
 * Parses a standard 5-field cron expression into a Schedule.
 *
 * Supports:
 *   *        — All values
 *   N        — Specific value
 *   N-M      — Range
 *   N-M/S    — Range with step
 *   * /S     — Every S (step)
 *   N,M,O    — List of values
 *
 * Named shortcuts: @yearly, @monthly, @weekly, @daily, @hourly
 */
public class Parser {

    private static final Map<String, String> SHORTCUTS = Map.of(
            "@yearly", "0 0 1 1 *",
            "@annually", "0 0 1 1 *",
            "@monthly", "0 0 1 * *",
            "@weekly", "0 0 * * 0",
            "@daily", "0 0 * * *",
            "@midnight", "0 0 * * *",
            "@hourly", "0 * * * *"
    );

    /**
     * Parse a cron expression string into a Schedule.
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Schedule parse(String expression) {
        String expr = expression.trim();

        // Check for named shortcuts
        if (SHORTCUTS.containsKey(expr.toLowerCase())) {
            expr = SHORTCUTS.get(expr.toLowerCase());
        }

        String[] fields = expr.split("\\s+");
        if (fields.length != 5) {
            throw new IllegalArgumentException(
                    "Cron expression must have 5 fields, got " + fields.length + ": " + expression);
        }

        Set<Integer> minutes = parseField(fields[0], 0, 59);
        Set<Integer> hours = parseField(fields[1], 0, 23);
        Set<Integer> daysOfMonth = parseField(fields[2], 1, 31);
        Set<Integer> months = parseField(fields[3], 1, 12);
        Set<Integer> daysOfWeek = parseField(fields[4], 0, 6);

        return new Schedule(expression, minutes, hours, daysOfMonth, months, daysOfWeek);
    }

    /**
     * Parse a single cron field into a set of integer values.
     */
    static Set<Integer> parseField(String field, int min, int max) {
        Set<Integer> values = new TreeSet<>();

        for (String part : field.split(",")) {
            part = part.trim();

            if (part.equals("*")) {
                // All values
                for (int i = min; i <= max; i++) values.add(i);
            } else if (part.contains("/")) {
                // Step: */5 or 1-30/5
                String[] stepParts = part.split("/");
                int step = Integer.parseInt(stepParts[1]);
                int start, end;

                if (stepParts[0].equals("*")) {
                    start = min;
                    end = max;
                } else if (stepParts[0].contains("-")) {
                    String[] range = stepParts[0].split("-");
                    start = Integer.parseInt(range[0]);
                    end = Integer.parseInt(range[1]);
                } else {
                    start = Integer.parseInt(stepParts[0]);
                    end = max;
                }

                for (int i = start; i <= end; i += step) {
                    values.add(i);
                }
            } else if (part.contains("-")) {
                // Range: 1-5
                String[] range = part.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (int i = start; i <= end; i++) {
                    values.add(i);
                }
            } else {
                // Single value
                values.add(Integer.parseInt(part));
            }
        }

        // Validate bounds
        for (int v : values) {
            if (v < min || v > max) {
                throw new IllegalArgumentException(
                        String.format("Value %d out of range [%d-%d] in field '%s'", v, min, max, field));
            }
        }

        return values;
    }
}
