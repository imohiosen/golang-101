package ratelimiter;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

/**
 * Entry point: HTTP server with rate-limited endpoints.
 *
 * Usage: java Main [--algo=token_bucket|sliding_window] [--limit=10] [--window=60000] [--port=8080]
 *
 * Test: curl -v http://localhost:8080/api/data  (repeat rapidly to see 429)
 *
 * Go structure:
 *   main.go                        — HTTP server setup, CLI args
 *   ratelimiter/token_bucket.go    — TokenBucket: TryAcquire, refill with time.Now()
 *   ratelimiter/sliding_window.go  — SlidingWindow: TryAcquire, prune, using []time.Time
 *   ratelimiter/middleware.go      — Middleware: per-client map, Check(clientID) → Result
 *                                     sync.Mutex for bucket/window access
 *   ratelimiter/handler.go         — HTTP handler wrapping rate limit check
 *
 * Rust structure:
 *   main.rs                        — hyper server setup
 *   token_bucket.rs                — TokenBucket with Instant::now() for timing
 *   sliding_window.rs              — SlidingWindow with VecDeque<Instant>
 *   middleware.rs                   — Middleware: HashMap<String, Limiter> behind Arc<Mutex<>>
 *   handler.rs                     — Request handler, extract client IP, apply limiter
 *
 * Key learning:
 *   - Go: time.Now(), sync.Mutex, map for per-client state, HTTP middleware pattern
 *   - Rust: Instant/Duration, Arc<Mutex<HashMap<>>>, VecDeque, hyper middleware
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Middleware.Algorithm algo = Middleware.Algorithm.TOKEN_BUCKET;
        int limit = 10;
        long windowMs = 60_000;
        int port = 8080;

        for (String arg : args) {
            if (arg.startsWith("--algo=")) {
                String val = arg.split("=")[1];
                algo = val.equals("sliding_window")
                        ? Middleware.Algorithm.SLIDING_WINDOW
                        : Middleware.Algorithm.TOKEN_BUCKET;
            } else if (arg.startsWith("--limit=")) {
                limit = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.startsWith("--window=")) {
                windowMs = Long.parseLong(arg.split("=")[1]);
            } else if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.split("=")[1]);
            }
        }

        Middleware rateLimiter = new Middleware(algo, limit, windowMs);
        Handler handler = new Handler(rateLimiter);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/data", handler::handle);
        server.start();

        System.out.printf("Rate limiter running on :%d (algo=%s, limit=%d, window=%dms)%n",
                port, algo, limit, windowMs);
    }
}
