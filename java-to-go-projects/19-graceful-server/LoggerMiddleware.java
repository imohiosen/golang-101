package graceful;

import java.time.Instant;

/**
 * Logging middleware that wraps request handlers.
 * Logs method, path, status code, and response time for each request.
 */
public class LoggerMiddleware {

    /**
     * Wrap a handler with request logging.
     */
    public static com.sun.net.httpserver.HttpHandler wrap(com.sun.net.httpserver.HttpHandler next) {
        return exchange -> {
            Instant start = Instant.now();
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();

            try {
                next.handle(exchange);
            } finally {
                long elapsed = java.time.Duration.between(start, Instant.now()).toMillis();
                System.out.printf("[%s] %s %s %s (%dms)%n",
                        Instant.now(), clientIp, method, path, elapsed);
            }
        };
    }
}
