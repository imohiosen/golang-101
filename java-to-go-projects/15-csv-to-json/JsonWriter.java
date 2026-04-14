package csv2json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Converts parsed CSV data into JSON output.
 * Supports both array-of-objects and array-of-arrays formats.
 */
public class JsonWriter {

    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Convert CSV data to JSON array of objects.
     * Each row becomes { "header1": value1, "header2": value2, ... }
     * Values are auto-typed using TypeDetector.
     */
    public static String toJsonObjects(CsvReader.CsvData data, boolean detectTypes) {
        List<Map<String, Object>> objects = new ArrayList<>();

        for (List<String> row : data.rows()) {
            Map<String, Object> obj = new LinkedHashMap<>();
            for (int i = 0; i < data.headers().size() && i < row.size(); i++) {
                String value = row.get(i);
                obj.put(data.headers().get(i),
                        detectTypes ? TypeDetector.convert(value) : value);
            }
            objects.add(obj);
        }

        try {
            return mapper.writeValueAsString(objects);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize JSON", e);
        }
    }

    /**
     * Convert CSV data to JSON array of arrays (compact format).
     */
    public static String toJsonArrays(CsvReader.CsvData data) {
        List<List<String>> arrays = new ArrayList<>();
        arrays.add(data.headers()); // first row = headers
        arrays.addAll(data.rows());

        try {
            return mapper.writeValueAsString(arrays);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize JSON", e);
        }
    }

    /**
     * Write JSON string to a file.
     */
    public static void writeToFile(String json, String filename) throws IOException {
        Files.writeString(Path.of(filename), json);
    }
}
