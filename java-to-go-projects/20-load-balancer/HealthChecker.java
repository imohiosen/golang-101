package loadbalancer;

import java.net.*;
import java.util.*;

/**
 * Periodically checks backend health via HTTP GET /health.
 * Marks backends as UP or DOWN based on response status.
 */
public class HealthChecker {

    private final List<Strategy.BackendState> backends;
    private final int intervalMs;
    private volatile boolean running = true;

    public HealthChecker(List<Strategy.BackendState> backends, int intervalMs) {
        this.backends = backends;
        this.intervalMs = intervalMs;
    }

    /**
     * Start health checking in a background thread.
     */
    public Thread start() {
        Thread t = new Thread(() -> {
            while (running) {
                for (Strategy.BackendState backend : backends) {
                    boolean healthy = checkHealth(backend.getUrl());
                    if (healthy != backend.isHealthy()) {
                        System.out.printf("Backend %s: %s → %s%n",
                                backend.getUrl(),
                                backend.isHealthy() ? "UP" : "DOWN",
                                healthy ? "UP" : "DOWN");
                    }
                    backend.setHealthy(healthy);
                }
                try {
                    Thread.sleep(intervalMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "health-checker");
        t.setDaemon(true);
        t.start();
        return t;
    }

    public void stop() {
        running = false;
    }

    private boolean checkHealth(String backendUrl) {
        try {
            URL url = new URI(backendUrl + "/health").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            int status = conn.getResponseCode();
            conn.disconnect();
            return status == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
