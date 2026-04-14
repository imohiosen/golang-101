package cron;

import java.time.LocalDateTime;
import java.util.*;

/**
 * CLI entry point for the cron expression parser.
 *
 * Usage: java Main "*/15 0 1,15 * 1-5" [--next=5]
 *
 * Go structure:
 *   main.go                  — CLI parsing, display
 *   cron/schedule.go         — Schedule struct, PrettyPrint
 *   cron/parser.go           — Parse(expr) → Schedule, parseField with range/step/list
 *   cron/next_time.go        — NextN(schedule, from, count), Matches(schedule, time)
 *                               Uses time.Time, time.Add(time.Minute)
 *
 * Rust structure:
 *   main.rs                  — CLI parsing
 *   schedule.rs              — Schedule struct, Display impl, HashSet<u32> for fields
 *   parser.rs                — parse(expr) → Schedule, parse_field with split/parse
 *   next_time.rs             — next_n(schedule, from, count) using chrono::NaiveDateTime
 *                               chrono crate for date/time manipulation
 *
 * Key learning:
 *   - Go: strings.Split, strconv.Atoi, time package, map[int]bool or bitset for fields
 *   - Rust: str::split, parse::<u32>(), HashSet, chrono crate
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: cron-parser <expression> [--next=5]");
            System.err.println("Example: cron-parser \"*/15 0 1,15 * 1-5\"");
            System.exit(1);
        }

        String expression = args[0];
        int nextCount = 5;

        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--next=")) {
                nextCount = Integer.parseInt(args[i].split("=")[1]);
            }
        }

        try {
            Schedule schedule = Parser.parse(expression);

            System.out.println("Cron: " + expression);
            System.out.println();
            System.out.println(schedule.prettyPrint());

            System.out.println();
            System.out.println("Next " + nextCount + " execution times:");
            List<LocalDateTime> times = NextTime.nextN(schedule, LocalDateTime.now(), nextCount);
            for (LocalDateTime t : times) {
                System.out.println("  " + t);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
