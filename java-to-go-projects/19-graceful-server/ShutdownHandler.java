package graceful;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages graceful shutdown of the server.
 *
 * On receiving a shutdown signal:
 *   1. Stop accepting new connections
 *   2. Wait for in-flight requests to complete (up to a timeout)
 *   3. Run cleanup hooks (close DB connections, flush logs, etc.)
 *   4. Exit
 */
public class ShutdownHandler {

    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);
    private final CountDownLatch shutdownComplete = new CountDownLatch(1);
    private final java.util.List<Runnable> hooks = new java.util.ArrayList<>();
    private final int timeoutSeconds;

    public ShutdownHandler(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Register a cleanup hook to run during shutdown.
     */
    public void addHook(Runnable hook) {
        hooks.add(hook);
    }

    /**
     * Check if the server is in shutdown mode.
     */
    public boolean isShuttingDown() {
        return shuttingDown.get();
    }

    /**
     * Initiate graceful shutdown.
     */
    public void shutdown(com.sun.net.httpserver.HttpServer server) {
        if (!shuttingDown.compareAndSet(false, true)) {
            return; // already shutting down
        }

        System.out.println("\nShutdown signal received...");

        // Stop accepting new connections, allow in-flight to finish
        System.out.printf("Waiting up to %ds for in-flight requests...%n", timeoutSeconds);
        server.stop(timeoutSeconds);

        // Run cleanup hooks
        System.out.println("Running cleanup hooks...");
        for (Runnable hook : hooks) {
            try {
                hook.run();
            } catch (Exception e) {
                System.err.println("Cleanup hook error: " + e.getMessage());
            }
        }

        System.out.println("Shutdown complete.");
        shutdownComplete.countDown();
    }

    /**
     * Block until shutdown is complete.
     */
    public void awaitShutdown() throws InterruptedException {
        shutdownComplete.await();
    }
}
