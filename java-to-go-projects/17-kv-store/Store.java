package kvstore;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory key-value store with thread-safe access.
 * Supports basic CRUD operations and key listing.
 */
public class Store {

    private final ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();

    public String get(String key) {
        return data.get(key);
    }

    public void set(String key, String value) {
        data.put(key, value);
    }

    public boolean delete(String key) {
        return data.remove(key) != null;
    }

    public Set<String> keys() {
        return data.keySet();
    }

    public int size() {
        return data.size();
    }

    /**
     * Replace all data with the given map (used by Persistence.load).
     */
    public void replaceAll(Map<String, String> newData) {
        data.clear();
        data.putAll(newData);
    }

    /**
     * Get a snapshot of all data (used by Persistence.save).
     */
    public Map<String, String> snapshot() {
        return new HashMap<>(data);
    }
}
