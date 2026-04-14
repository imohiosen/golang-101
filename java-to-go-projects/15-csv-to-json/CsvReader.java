package csv2json;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Reads a CSV file into a list of rows (each row = list of strings).
 * Handles quoted fields, escaped quotes, and custom delimiters.
 */
public class CsvReader {

    private final char delimiter;
    private final boolean hasHeader;

    public CsvReader(char delimiter, boolean hasHeader) {
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
    }

    public CsvReader() {
        this(',', true);
    }

    /**
     * Parse a CSV file. Returns headers (if present) and data rows.
     */
    public CsvData read(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        if (lines.isEmpty()) {
            return new CsvData(List.of(), List.of());
        }

        List<String> headers;
        int startRow;
        if (hasHeader) {
            headers = parseLine(lines.get(0));
            startRow = 1;
        } else {
            int colCount = parseLine(lines.get(0)).size();
            headers = new ArrayList<>();
            for (int i = 0; i < colCount; i++) {
                headers.add("col_" + i);
            }
            startRow = 0;
        }

        List<List<String>> rows = new ArrayList<>();
        for (int i = startRow; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            rows.add(parseLine(line));
        }

        return new CsvData(headers, rows);
    }

    /**
     * Parse a single CSV line respecting quoted fields.
     */
    private List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++; // skip escaped quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == delimiter) {
                    fields.add(current.toString().trim());
                    current = new StringBuilder();
                } else {
                    current.append(c);
                }
            }
        }
        fields.add(current.toString().trim());
        return fields;
    }

    public record CsvData(List<String> headers, List<List<String>> rows) {}
}
