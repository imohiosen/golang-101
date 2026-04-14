package proxy;

import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * Core proxy logic: receives an incoming request, forwards it to the target,
 * and returns the response. Adds configured headers and handles errors.
 */
public class Proxy {

    private final Rewriter rewriter;
    private final Logger logger;
    private final Config config;

    public Proxy(Config config, Rewriter rewriter, Logger logger) {
        this.config = config;
        this.rewriter = rewriter;
        this.logger = logger;
    }

    /**
     * Forward a request to the target backend.
     * Called by the HTTP server handler for each incoming request.
     */
    public ProxyResponse forward(String method, String path, Map<String, String> headers,
                                  byte[] body, String clientIp) {
        logger.logRequest(method, path, clientIp);
        long start = System.currentTimeMillis();

        try {
            String targetUrl = rewriter.rewrite(path);
            URL url = new URI(targetUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(config.getTimeoutMs());
            conn.setReadTimeout(config.getTimeoutMs());

            // Copy request headers
            headers.forEach(conn::setRequestProperty);
            // Add configured extra headers
            config.getAddHeaders().forEach(conn::setRequestProperty);

            // Send body if present
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
            logger.logResponse(method, path, status, elapsed);

            return new ProxyResponse(status, responseBody, conn.getHeaderFields());

        } catch (Exception e) {
            logger.logError(method, path, e.getMessage());
            return new ProxyResponse(502,
                    ("{\"error\":\"" + e.getMessage() + "\"}").getBytes(), Map.of());
        }
    }

    public record ProxyResponse(int statusCode, byte[] body, Map<String, ?> headers) {}
}
