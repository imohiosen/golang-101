package pipeline;

import java.util.function.Function;

/**
 * A Channel connects two pipeline stages.
 * It's a bounded, blocking, thread-safe queue for passing data between stages.
 *
 * In Go: this maps directly to a buffered channel (chan T).
 * In Rust: this maps to crossbeam::channel or tokio::sync::mpsc.
 */
public class Channel<T> {

    private final java.util.concurrent.BlockingQueue<T> queue;
    private volatile boolean closed = false;

    public Channel(int capacity) {
        this.queue = new java.util.concurrent.ArrayBlockingQueue<>(capacity);
    }

    public Channel() {
        this(100);
    }

    /**
     * Send a value into the channel. Blocks if full.
     */
    public void send(T value) throws InterruptedException {
        if (closed) throw new IllegalStateException("Channel is closed");
        queue.put(value);
    }

    /**
     * Receive a value from the channel. Returns null if closed and empty.
     */
    public T receive() throws InterruptedException {
        if (closed && queue.isEmpty()) return null;
        return queue.poll(100, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    /**
     * Close the channel. No more sends allowed.
     */
    public void close() {
        this.closed = true;
    }

    public boolean isClosed() {
        return closed && queue.isEmpty();
    }

    public int size() { return queue.size(); }
}
