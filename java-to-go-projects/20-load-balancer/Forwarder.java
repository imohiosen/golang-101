package loadbalancer;

import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * Forwards HTTP requests to a selected backend.
 * Copies request headers and body, returns the backend's response.
 */
public class Forwarder {

    private final int timeoutMs;

    public Forwarder(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    /**
     * Forward request to the given backend URL.
     */
    public ForwardResult forward(Strategy.BackendState backend, String method, String path,
                                  Map<String, String> headers, byte[] body) {
        backend.incrementConnections();
        long start = System.currentTimeMillis();

        try {
            String targetUrl = backend.getUrl() + path;
            URL url = new URI(targetUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(timeoutMs);
            conn.setReadTimeout(timeoutMs);

            // Copy headers
            headers.forEach(conn::setRequestProperty);
            conn.setRequestProperty("X-Forwarded-For", "load-balancer");
            conn.setRequestProperty("X-Backend", backend.getUrl());

            // Send body
            if (body != null && body.length > 0) {
                conn.setDoOutput(true);
                conn.getOutputStream().write(body);
            }

            int status = conn.getResponseCode();
            InputStream responseStream = (status >= 400)
                    ? conn.getErrorStream() : conn.getInputStream();
            byte[] responseBody = responseStream != null
                    ? responseStream.readAllBytes() : new byte[0];

            long elapsed = System.currentTimeMillis() - start;
            return new ForwardResult(status, responseBody, elapsed, null);

        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - start;
            return new ForwardResult(502,
                    ("{\"error\":\"" + e.getMessage() + "\"}").getBytes(),
                    elapsed, e.getMessage());
        } finally {
            backend.decrementConnections();
        }
    }

    public record ForwardResult(int statusCode, byte[] body, long durationMs, String error) {
        public boolean isSuccess() { return error == null; }
    }
}
