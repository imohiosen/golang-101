package graceful;

/**
 * Entry point: HTTP server with graceful shutdown.
 *
 * Usage: java Main [--port=8080] [--timeout=30]
 *
 * Test:
 *   1. Start server
 *   2. curl http://localhost:8080/slow &     (starts 5s request)
 *   3. Ctrl+C the server                     (triggers shutdown)
 *   4. Server waits for /slow to complete before exiting
 *
 * Go structure:
 *   main.go                          — Setup server, signal handling with os/signal
 *   graceful/server.go               — Server: setup routes, ListenAndServe
 *   graceful/handler.go              — Health, Slow, Echo handlers
 *   graceful/shutdown.go             — Signal listener (SIGINT/SIGTERM), context.WithTimeout
 *                                       http.Server.Shutdown(ctx) for graceful drain
 *   graceful/logger_middleware.go    — Middleware: wraps http.Handler, logs req/res
 *
 *   Key Go pattern:
 *     ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
 *     server.Shutdown(ctx)  — drains in-flight, refuses new connections
 *
 * Rust structure:
 *   main.rs                          — Setup, tokio::signal for Ctrl+C
 *   server.rs                        — hyper server with graceful::shutdown
 *   handler.rs                       — health, slow, echo handlers
 *   shutdown.rs                      — tokio::signal::ctrl_c(), notify via watch channel
 *                                       hyper::server::conn::http1 with graceful shutdown
 *   logger_middleware.rs             — Tower middleware or manual wrapper
 *
 *   Key Rust pattern:
 *     hyper_util::server::graceful::GracefulShutdown
 *     tokio::select! { _ = server => {}, _ = signal => { shutdown } }
 *
 * Key learning:
 *   - Go: os/signal.Notify, context for timeouts, http.Server.Shutdown
 *   - Rust: tokio::signal, graceful shutdown patterns, select! macro
 */
public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        int timeout = 30;

        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.startsWith("--timeout=")) {
                timeout = Integer.parseInt(arg.split("=")[1]);
            }
        }

        Server server = new Server(port, timeout);

        // Register cleanup hooks
        server.getShutdownHandler().addHook(() -> System.out.println("  → Flushing logs..."));
        server.getShutdownHandler().addHook(() -> System.out.println("  → Closing DB connections..."));

        server.registerShutdownHook();
        server.start();

        // Keep main thread alive
        server.getShutdownHandler().awaitShutdown();
    }
}
