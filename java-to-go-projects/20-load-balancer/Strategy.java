package loadbalancer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Selects a backend based on the configured strategy.
 * Supports round-robin, least-connections, and random strategies.
 */
public class Strategy {

    public enum Type {
        ROUND_ROBIN, LEAST_CONNECTIONS, RANDOM
    }

    private final Type type;
    private final AtomicInteger roundRobinIndex = new AtomicInteger(0);
    private final Random random = new Random();

    public Strategy(String name) {
        this.type = switch (name.toLowerCase()) {
            case "least-connections", "least_connections" -> Type.LEAST_CONNECTIONS;
            case "random" -> Type.RANDOM;
            default -> Type.ROUND_ROBIN;
        };
    }

    /**
     * Select a backend from the list of healthy backends.
     * Returns null if no backends are available.
     */
    public BackendState select(List<BackendState> healthy) {
        if (healthy.isEmpty()) return null;

        return switch (type) {
            case ROUND_ROBIN -> {
                int idx = roundRobinIndex.getAndIncrement() % healthy.size();
                yield healthy.get(idx);
            }
            case LEAST_CONNECTIONS -> healthy.stream()
                    .min(Comparator.comparingInt(BackendState::getActiveConnections))
                    .orElse(null);
            case RANDOM -> healthy.get(random.nextInt(healthy.size()));
        };
    }

    /**
     * Tracks state for a single backend (health, connections, stats).
     */
    public static class BackendState {
        private final Config.Backend backend;
        private volatile boolean healthy = true;
        private final AtomicInteger activeConnections = new AtomicInteger(0);
        private final AtomicInteger totalRequests = new AtomicInteger(0);

        public BackendState(Config.Backend backend) {
            this.backend = backend;
        }

        public Config.Backend getBackend() { return backend; }
        public String getUrl() { return backend.getUrl(); }
        public boolean isHealthy() { return healthy; }
        public void setHealthy(boolean healthy) { this.healthy = healthy; }
        public int getActiveConnections() { return activeConnections.get(); }
        public void incrementConnections() { activeConnections.incrementAndGet(); totalRequests.incrementAndGet(); }
        public void decrementConnections() { activeConnections.decrementAndGet(); }
        public int getTotalRequests() { return totalRequests.get(); }

        @Override
        public String toString() {
            return String.format("%s [%s] active=%d total=%d",
                    backend.getUrl(), healthy ? "UP" : "DOWN",
                    activeConnections.get(), totalRequests.get());
        }
    }
}
