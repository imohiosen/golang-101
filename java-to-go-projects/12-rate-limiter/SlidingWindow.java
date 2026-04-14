package ratelimiter;

import java.time.Instant;
import java.util.*;

/**
 * Sliding window rate limiter.
 * Tracks request timestamps; allows N requests per time window.
 * Old entries are pruned on each check.
 */
public class SlidingWindow {

    private final int maxRequests;
    private final long windowMs;
    private final LinkedList<Long> timestamps = new LinkedList<>();
    private final Object lock = new Object();

    /**
     * @param maxRequests Maximum requests allowed in the window
     * @param windowMs   Window size in milliseconds
     */
    public SlidingWindow(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
    }

    /**
     * Check if a request is allowed. If yes, records it.
     */
    public boolean tryAcquire() {
        synchronized (lock) {
            long now = System.currentTimeMillis();
            prune(now);

            if (timestamps.size() < maxRequests) {
                timestamps.add(now);
                return true;
            }
            return false;
        }
    }

    /**
     * How many requests remain in the current window.
     */
    public int remainingRequests() {
        synchronized (lock) {
            prune(System.currentTimeMillis());
            return Math.max(0, maxRequests - timestamps.size());
        }
    }

    /**
     * Milliseconds until the next slot opens.
     */
    public long retryAfterMs() {
        synchronized (lock) {
            if (timestamps.isEmpty()) return 0;
            long oldest = timestamps.getFirst();
            long expiresAt = oldest + windowMs;
            return Math.max(0, expiresAt - System.currentTimeMillis());
        }
    }

    private void prune(long now) {
        while (!timestamps.isEmpty() && timestamps.getFirst() < now - windowMs) {
            timestamps.removeFirst();
        }
    }
}
