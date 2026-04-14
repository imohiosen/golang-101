package proxy;

import java.util.*;

/**
 * Configuration for the HTTP proxy.
 * Loaded from a JSON config file or CLI args.
 */
public class Config {
    private int port = 8080;
    private String targetHost = "http://localhost:3000";
    private List<RewriteRule> rewriteRules = new ArrayList<>();
    private boolean logRequests = true;
    private int timeoutMs = 30_000;
    private Map<String, String> addHeaders = new HashMap<>();

    public static class RewriteRule {
        private String pathPrefix;
        private String target;

        public RewriteRule() {}
        public RewriteRule(String pathPrefix, String target) {
            this.pathPrefix = pathPrefix;
            this.target = target;
        }

        public String getPathPrefix() { return pathPrefix; }
        public void setPathPrefix(String pathPrefix) { this.pathPrefix = pathPrefix; }
        public String getTarget() { return target; }
        public void setTarget(String target) { this.target = target; }
    }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public String getTargetHost() { return targetHost; }
    public void setTargetHost(String targetHost) { this.targetHost = targetHost; }
    public List<RewriteRule> getRewriteRules() { return rewriteRules; }
    public void setRewriteRules(List<RewriteRule> rules) { this.rewriteRules = rules; }
    public boolean isLogRequests() { return logRequests; }
    public void setLogRequests(boolean logRequests) { this.logRequests = logRequests; }
    public int getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; }
    public Map<String, String> getAddHeaders() { return addHeaders; }
    public void setAddHeaders(Map<String, String> addHeaders) { this.addHeaders = addHeaders; }

    /**
     * Load config from a JSON file.
     */
    public static Config loadFromFile(String filename) throws Exception {
        var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        return mapper.readValue(java.nio.file.Files.readString(java.nio.file.Path.of(filename)), Config.class);
    }
}
