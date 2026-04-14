package ratelimiter;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.time.Instant;

/**
 * Token bucket rate limiter.
 * Tokens are added at a fixed rate; each request consumes one token.
 * If no tokens are available, the request is rejected.
 */
public class TokenBucket {

    private final int capacity;
    private final double refillRate; // tokens per second
    private double tokens;
    private Instant lastRefill;
    private final Object lock = new Object();

    /**
     * @param capacity   Maximum tokens the bucket can hold
     * @param refillRate Tokens added per second
     */
    public TokenBucket(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity; // start full
        this.lastRefill = Instant.now();
    }

    /**
     * Try to consume one token. Returns true if allowed, false if rate limited.
     */
    public boolean tryAcquire() {
        synchronized (lock) {
            refill();
            if (tokens >= 1.0) {
                tokens -= 1.0;
                return true;
            }
            return false;
        }
    }

    /**
     * How many tokens are currently available.
     */
    public double availableTokens() {
        synchronized (lock) {
            refill();
            return tokens;
        }
    }

    private void refill() {
        Instant now = Instant.now();
        double elapsed = (now.toEpochMilli() - lastRefill.toEpochMilli()) / 1000.0;
        tokens = Math.min(capacity, tokens + elapsed * refillRate);
        lastRefill = now;
    }
}
