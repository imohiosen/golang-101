package kvstore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Handles saving and loading the KV store to/from disk.
 * Uses JSON format for human-readable persistence.
 */
public class Persistence {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final String filePath;

    public Persistence(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Save the store's data to disk as JSON.
     */
    public void save(Store store) throws IOException {
        Map<String, String> data = store.snapshot();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        Files.writeString(Path.of(filePath), json);
        System.out.printf("Saved %d entries to %s%n", data.size(), filePath);
    }

    /**
     * Load data from disk into the store (replaces existing data).
     */
    public void load(Store store) throws IOException {
        if (!Files.exists(Path.of(filePath))) {
            System.out.println("No save file found: " + filePath);
            return;
        }

        String json = Files.readString(Path.of(filePath));
        Map<String, String> data = mapper.readValue(json, new TypeReference<>() {});
        store.replaceAll(data);
        System.out.printf("Loaded %d entries from %s%n", data.size(), filePath);
    }
}
