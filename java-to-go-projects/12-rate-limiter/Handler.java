package ratelimiter;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;

/**
 * Simple HTTP handler that serves a protected endpoint.
 * Used to demonstrate rate limiting in action.
 */
public class Handler {

    private final Middleware rateLimiter;

    public Handler(Middleware rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    /**
     * Handle an HTTP request with rate limiting applied.
     */
    public void handle(HttpExchange exchange) throws IOException {
        String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();

        Middleware.RateLimitResult result = rateLimiter.check(clientIp);

        // Set rate limit headers on all responses
        result.toHeaders().forEach((k, v) ->
                exchange.getResponseHeaders().add(k, v));

        if (!result.allowed()) {
            String body = "{\"error\":\"rate limit exceeded\",\"retry_after_ms\":" + result.retryAfterMs() + "}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(429, body.length());
            exchange.getResponseBody().write(body.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        String body = "{\"message\":\"OK\",\"remaining\":" + result.remaining() + "}";
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length());
        exchange.getResponseBody().write(body.getBytes());
        exchange.getResponseBody().close();
    }
}
