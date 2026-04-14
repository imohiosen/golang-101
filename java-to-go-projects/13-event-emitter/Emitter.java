package events;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * A thread-safe event emitter supporting:
 *   - on(event, callback)           — Add a persistent listener
 *   - once(event, callback)         — Add a one-shot listener
 *   - off(event, listenerId)        — Remove a listener by ID
 *   - emit(event, data)             — Fire event to all listeners
 *   - listenerCount(event)          — Count listeners for an event
 *   - eventNames()                  — List all registered event names
 *
 * Supports wildcard event "*" that receives all events.
 */
public class Emitter<T> {

    private final Map<String, List<Listener<T>>> listeners = new ConcurrentHashMap<>();
    private int nextId = 0;

    /**
     * Register a persistent listener for an event.
     * Returns the listener ID for later removal.
     */
    public String on(String event, Consumer<T> callback) {
        return addListener(event, callback, false);
    }

    /**
     * Register a one-shot listener (auto-removed after first emit).
     */
    public String once(String event, Consumer<T> callback) {
        return addListener(event, callback, true);
    }

    /**
     * Remove a specific listener by ID from an event.
     */
    public boolean off(String event, String listenerId) {
        List<Listener<T>> list = listeners.get(event);
        if (list == null) return false;
        return list.removeIf(l -> l.getId().equals(listenerId));
    }

    /**
     * Remove ALL listeners for an event.
     */
    public void offAll(String event) {
        listeners.remove(event);
    }

    /**
     * Emit an event with associated data. Invokes all matching listeners.
     * Also invokes wildcard "*" listeners.
     */
    public void emit(String event, T data) {
        fire(event, data);
        if (!"*".equals(event)) {
            fire("*", data); // wildcard listeners
        }
    }

    /**
     * Number of listeners registered for a specific event.
     */
    public int listenerCount(String event) {
        List<Listener<T>> list = listeners.get(event);
        return list == null ? 0 : list.size();
    }

    /**
     * All event names that have at least one listener.
     */
    public Set<String> eventNames() {
        return listeners.keySet();
    }

    private void fire(String event, T data) {
        List<Listener<T>> list = listeners.get(event);
        if (list == null) return;

        List<Listener<T>> toRemove = new ArrayList<>();
        for (Listener<T> listener : list) {
            boolean shouldRemove = listener.invoke(data);
            if (shouldRemove) {
                toRemove.add(listener);
            }
        }
        list.removeAll(toRemove);

        if (list.isEmpty()) {
            listeners.remove(event);
        }
    }

    private String addListener(String event, Consumer<T> callback, boolean once) {
        String id = "listener_" + (nextId++);
        listeners.computeIfAbsent(event, k -> new CopyOnWriteArrayList<>())
                 .add(new Listener<>(id, callback, once));
        return id;
    }
}
