package loadbalancer;

import java.util.*;

/**
 * Configuration for the load balancer.
 */
public class Config {

    private int port = 8080;
    private List<Backend> backends = new ArrayList<>();
    private String strategy = "round-robin"; // round-robin, least-connections, random
    private int healthCheckIntervalMs = 5000;
    private int timeoutMs = 10_000;

    public static class Backend {
        private String url;
        private int weight = 1;

        public Backend() {}
        public Backend(String url) { this.url = url; }
        public Backend(String url, int weight) { this.url = url; this.weight = weight; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public int getWeight() { return weight; }
        public void setWeight(int weight) { this.weight = weight; }

        @Override
        public String toString() { return url + " (weight=" + weight + ")"; }
    }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public List<Backend> getBackends() { return backends; }
    public void setBackends(List<Backend> backends) { this.backends = backends; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public int getHealthCheckIntervalMs() { return healthCheckIntervalMs; }
    public void setHealthCheckIntervalMs(int ms) { this.healthCheckIntervalMs = ms; }
    public int getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(int ms) { this.timeoutMs = ms; }
}
