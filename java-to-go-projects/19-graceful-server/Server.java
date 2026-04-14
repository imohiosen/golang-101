package graceful;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * HTTP server with graceful shutdown, middleware, and clean architecture.
 */
public class Server {

    private final int port;
    private final ShutdownHandler shutdownHandler;
    private HttpServer httpServer;

    public Server(int port, int shutdownTimeoutSeconds) {
        this.port = port;
        this.shutdownHandler = new ShutdownHandler(shutdownTimeoutSeconds);
    }

    public ShutdownHandler getShutdownHandler() { return shutdownHandler; }

    /**
     * Configure routes and start the server.
     */
    public void start() throws Exception {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(10));

        // Register routes with middleware
        httpServer.createContext("/health", LoggerMiddleware.wrap(Handler::health));
        httpServer.createContext("/slow", LoggerMiddleware.wrap(Handler::slow));
        httpServer.createContext("/echo", LoggerMiddleware.wrap(Handler::echo));

        httpServer.start();
        System.out.printf("Server running on :%d%n", port);
        System.out.println("Endpoints: /health, /slow, /echo");
        System.out.println("Press Ctrl+C to trigger graceful shutdown");
    }

    /**
     * Register JVM shutdown hook for graceful exit on SIGTERM/SIGINT.
     */
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdownHandler.shutdown(httpServer);
        }));
    }
}
