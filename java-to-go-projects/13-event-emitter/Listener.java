package events;

import java.util.function.Consumer;

/**
 * A listener wraps a callback function and optional metadata.
 * Can be one-shot (auto-removed after first invocation) or persistent.
 */
public class Listener<T> {

    private final String id;
    private final Consumer<T> callback;
    private final boolean once;
    private int invokeCount = 0;

    public Listener(String id, Consumer<T> callback, boolean once) {
        this.id = id;
        this.callback = callback;
        this.once = once;
    }

    /**
     * Invoke the callback with the given data. Returns true if this listener
     * should be removed (one-shot listener that has been called).
     */
    public boolean invoke(T data) {
        callback.accept(data);
        invokeCount++;
        return once;
    }

    public String getId() { return id; }
    public boolean isOnce() { return once; }
    public int getInvokeCount() { return invokeCount; }

    @Override
    public String toString() {
        return String.format("Listener{id='%s', once=%s, invoked=%d}", id, once, invokeCount);
    }
}
