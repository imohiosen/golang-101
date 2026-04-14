package loadbalancer;

import java.util.*;

/**
 * Entry point: HTTP reverse-proxy load balancer.
 *
 * Usage: java Main [--port=8080] [--strategy=round-robin]
 *                  [--backend=http://localhost:3001]
 *                  [--backend=http://localhost:3002]
 *
 * Go structure:
 *   main.go                       — CLI args, build Config, start Balancer
 *   loadbalancer/config.go        — Config, Backend structs
 *   loadbalancer/strategy.go      — Strategy interface + RoundRobin/LeastConn/Random impls
 *                                    BackendState with sync/atomic for counters
 *   loadbalancer/health.go        — HealthChecker: goroutine, ticker, net/http.Get /health
 *   loadbalancer/forwarder.go     — Forward(backend, req) using net/http.Client
 *                                    Copies headers, body, sets X-Forwarded-For
 *   loadbalancer/balancer.go      — Balancer: HTTP handler, select backend, forward, /lb/status
 *
 * Rust structure:
 *   main.rs                       — CLI args, build Config, start
 *   config.rs                     — Config, Backend (Deserialize)
 *   strategy.rs                   — Strategy enum + select(), BackendState with AtomicUsize
 *   health.rs                     — tokio::spawn health check loop, reqwest::get
 *   forwarder.rs                  — forward() using reqwest::Client
 *   balancer.rs                   — hyper handler, backend selection, forwarding, /lb/status
 *
 * Key learning:
 *   - Go: atomic.Int64 for counters, sync.RWMutex for health state, httputil.ReverseProxy
 *   - Rust: AtomicUsize, Arc<RwLock<Vec<BackendState>>>, reqwest, hyper middleware
 *   - Both: Round-robin with atomic counter, health check patterns, header forwarding
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        List<String> backendUrls = new ArrayList<>();

        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                config.setPort(Integer.parseInt(arg.split("=")[1]));
            } else if (arg.startsWith("--strategy=")) {
                config.setStrategy(arg.split("=", 2)[1]);
            } else if (arg.startsWith("--backend=")) {
                backendUrls.add(arg.split("=", 2)[1]);
            }
        }

        if (backendUrls.isEmpty()) {
            // Default demo backends
            backendUrls.add("http://localhost:3001");
            backendUrls.add("http://localhost:3002");
            backendUrls.add("http://localhost:3003");
        }

        List<Config.Backend> backends = backendUrls.stream()
                .map(Config.Backend::new)
                .toList();
        config.setBackends(backends);

        Balancer balancer = new Balancer(config);
        balancer.start();
    }
}
