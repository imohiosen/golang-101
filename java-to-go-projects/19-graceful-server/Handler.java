package graceful;

import com.sun.net.httpserver.*;
import java.io.*;

/**
 * Application-level HTTP handlers.
 * These are the actual business logic endpoints.
 */
public class Handler {

    /**
     * Health check endpoint — returns 200 with status.
     */
    public static void health(HttpExchange exchange) throws IOException {
        String body = "{\"status\":\"healthy\"}";
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length());
        exchange.getResponseBody().write(body.getBytes());
        exchange.getResponseBody().close();
    }

    /**
     * Simulates a slow endpoint (for testing graceful shutdown with in-flight requests).
     */
    public static void slow(HttpExchange exchange) throws IOException {
        try {
            Thread.sleep(5000); // simulate slow work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String body = "{\"message\":\"slow response complete\"}";
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length());
        exchange.getResponseBody().write(body.getBytes());
        exchange.getResponseBody().close();
    }

    /**
     * Echo endpoint — returns request info.
     */
    public static void echo(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().toString();
        String body = String.format("{\"method\":\"%s\",\"path\":\"%s\"}", method, path);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length());
        exchange.getResponseBody().write(body.getBytes());
        exchange.getResponseBody().close();
    }
}
