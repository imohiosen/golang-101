package proxy;

import java.time.Instant;
import java.time.Duration;

/**
 * Logs incoming requests and outgoing responses with timing.
 * Formats output for console display.
 */
public class Logger {

    private boolean enabled;

    public Logger(boolean enabled) {
        this.enabled = enabled;
    }

    public void logRequest(String method, String path, String clientIp) {
        if (!enabled) return;
        System.out.printf("[%s] → %s %s (from %s)%n",
                Instant.now(), method, path, clientIp);
    }

    public void logResponse(String method, String path, int statusCode, long durationMs) {
        if (!enabled) return;
        System.out.printf("[%s] ← %s %s → %d (%dms)%n",
                Instant.now(), method, path, statusCode, durationMs);
    }

    public void logError(String method, String path, String error) {
        System.err.printf("[%s] ✗ %s %s → ERROR: %s%n",
                Instant.now(), method, path, error);
    }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
