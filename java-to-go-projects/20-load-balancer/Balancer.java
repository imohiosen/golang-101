package loadbalancer;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Core load balancer that ties together strategy, health checking, and forwarding.
 */
public class Balancer {

    private final Config config;
    private final List<Strategy.BackendState> backends;
    private final Strategy strategy;
    private final Forwarder forwarder;
    private final HealthChecker healthChecker;

    public Balancer(Config config) {
        this.config = config;
        this.backends = config.getBackends().stream()
                .map(Strategy.BackendState::new)
                .collect(Collectors.toList());
        this.strategy = new Strategy(config.getStrategy());
        this.forwarder = new Forwarder(config.getTimeoutMs());
        this.healthChecker = new HealthChecker(backends, config.getHealthCheckIntervalMs());
    }

    /**
     * Start the load balancer HTTP server + health checker.
     */
    public void start() throws IOException {
        // Start health checker
        healthChecker.start();

        // Start HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(config.getPort()), 0);
        server.setExecutor(Executors.newFixedThreadPool(20));

        // /status endpoint — shows backend states
        server.createContext("/lb/status", this::handleStatus);

        // All other requests — proxy to backends
        server.createContext("/", this::handleProxy);

        server.start();
        System.out.printf("Load balancer on :%d — strategy=%s — %d backends%n",
                config.getPort(), config.getStrategy(), backends.size());
        backends.forEach(b -> System.out.println("  " + b));
    }

    private void handleProxy(HttpExchange exchange) throws IOException {
        List<Strategy.BackendState> healthy = backends.stream()
                .filter(Strategy.BackendState::isHealthy)
                .collect(Collectors.toList());

        Strategy.BackendState selected = strategy.select(healthy);

        if (selected == null) {
            String body = "{\"error\":\"no healthy backends\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(503, body.length());
            exchange.getResponseBody().write(body.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().toString();
        Map<String, String> headers = new HashMap<>();
        exchange.getRequestHeaders().forEach((k, v) -> headers.put(k, v.get(0)));
        byte[] body = exchange.getRequestBody().readAllBytes();

        Forwarder.ForwardResult result = forwarder.forward(selected, method, path, headers, body);

        exchange.getResponseHeaders().add("X-Backend", selected.getUrl());
        exchange.sendResponseHeaders(result.statusCode(), result.body().length);
        exchange.getResponseBody().write(result.body());
        exchange.getResponseBody().close();
    }

    private void handleStatus(HttpExchange exchange) throws IOException {
        StringBuilder sb = new StringBuilder("{\"backends\":[");
        for (int i = 0; i < backends.size(); i++) {
            if (i > 0) sb.append(",");
            Strategy.BackendState b = backends.get(i);
            sb.append(String.format("{\"url\":\"%s\",\"healthy\":%s,\"active\":%d,\"total\":%d}",
                    b.getUrl(), b.isHealthy(), b.getActiveConnections(), b.getTotalRequests()));
        }
        sb.append("]}");
        String body = sb.toString();
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length());
        exchange.getResponseBody().write(body.getBytes());
        exchange.getResponseBody().close();
    }
}
