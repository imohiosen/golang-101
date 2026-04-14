import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Log Parser — Reads a log file, extracts structured data using regex.
 *
 * Concepts: File I/O, regex parsing, string manipulation, data aggregation
 *
 * Input: A log file with lines like:
 *   [2024-01-15 10:23:45] ERROR /api/users - Connection timeout
 *   [2024-01-15 10:24:01] INFO  /api/health - OK
 *   [2024-01-15 10:24:15] WARN  /api/orders - Slow query (2.3s)
 *
 * Features:
 *   1. Parse each line into: timestamp, level, path, message
 *   2. Filter by log level (ERROR, WARN, INFO)
 *   3. Count occurrences per level
 *   4. Find all unique paths
 */
public class LogParser {

    record LogEntry(String timestamp, String level, String path, String message) {}

    private static final Pattern LOG_PATTERN =
        Pattern.compile("\\[(.*?)\\] (\\w+)\\s+(\\S+) - (.*)");

    public static List<LogEntry> parseFile(String filename) throws IOException {
        List<LogEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = LOG_PATTERN.matcher(line);
                if (m.matches()) {
                    entries.add(new LogEntry(m.group(1), m.group(2), m.group(3), m.group(4)));
                }
            }
        } v
vccc
        return entries;
    }

    public static List<LogEntry> filterByLevel(List<LogEntry> entries, String level) {
        return entries.stream()
            .filter(e -> e.level().equalsIgnoreCase(level))
            .toList();
    }

    public static Map<String, Long> countByLevel(List<LogEntry> entries) {
        Map<String, Long> counts = new HashMap<>();
        for (LogEntry e : entries) {
            counts.merge(e.level(), 1L, Long::sum);
        }
        return counts;
    }

    public static Set<String> uniquePaths(List<LogEntry> entries) {
        Set<String> paths = new HashSet<>();
        for (LogEntry e : entries) {
            paths.add(e.path());
        }
        return paths;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java LogParser <logfile>");
            return;
        }
        List<LogEntry> entries = parseFile(args[0]);
        System.out.println("Total entries: " + entries.size());
        System.out.println("Counts: " + countByLevel(entries));
        System.out.println("Errors: " + filterByLevel(entries, "ERROR").size());
        System.out.println("Paths: " + uniquePaths(entries));
    }
}
