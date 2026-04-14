package ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HTTP middleware that applies per-client rate limiting.
 * Uses client IP (or API key) as the identifier.
 * Supports pluggable algorithm (token bucket or sliding window).
 */
public class Middleware {

    public enum Algorithm { TOKEN_BUCKET, SLIDING_WINDOW }

    private final Algorithm algorithm;
    private final int limit;
    private final long windowMs;

    // Per-client limiters
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private final Map<String, SlidingWindow> windows = new ConcurrentHashMap<>();

    public Middleware(Algorithm algorithm, int limit, long windowMs) {
        this.algorithm = algorithm;
        this.limit = limit;
        this.windowMs = windowMs;
    }

    /**
     * Check if a request from the given client is allowed.
     * Returns a RateLimitResult with allowed status and headers.
     */
    public RateLimitResult check(String clientId) {
        boolean allowed;
        int remaining;
        long retryAfter;

        if (algorithm == Algorithm.TOKEN_BUCKET) {
            TokenBucket bucket = buckets.computeIfAbsent(clientId,
                    k -> new TokenBucket(limit, (double) limit / (windowMs / 1000.0)));
            allowed = bucket.tryAcquire();
            remaining = (int) bucket.availableTokens();
            retryAfter = allowed ? 0 : (long) (1000.0 / ((double) limit / (windowMs / 1000.0)));
        } else {
            SlidingWindow window = windows.computeIfAbsent(clientId,
                    k -> new SlidingWindow(limit, windowMs));
            allowed = window.tryAcquire();
            remaining = window.remainingRequests();
            retryAfter = allowed ? 0 : window.retryAfterMs();
        }

        return new RateLimitResult(allowed, limit, remaining, retryAfter);
    }

    public record RateLimitResult(boolean allowed, int limit, int remaining, long retryAfterMs) {
        public Map<String, String> toHeaders() {
            var headers = new java.util.HashMap<String, String>();
            headers.put("X-RateLimit-Limit", String.valueOf(limit));
            headers.put("X-RateLimit-Remaining", String.valueOf(remaining));
            if (!allowed) {
                headers.put("Retry-After", String.valueOf(retryAfterMs / 1000));
            }
            return headers;
        }
    }
}
