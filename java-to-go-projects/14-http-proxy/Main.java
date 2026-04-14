package proxy;

import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * Entry point: starts the reverse proxy HTTP server.
 *
 * Usage: java Main [--config=proxy.json] [--port=8080] [--target=http://localhost:3000]
 *
 * Go structure:
 *   main.go                — HTTP server (net/http), CLI args
 *   proxy/config.go        — Config struct, LoadFromFile (encoding/json)
 *   proxy/rewriter.go      — Rewrite(path) → targetURL using prefix rules
 *   proxy/proxy.go         — Forward(method, path, headers, body) using net/http.Client
 *                             httputil.ReverseProxy alternative discussion
 *   proxy/logger.go        — LogRequest, LogResponse with timing
 *
 * Rust structure:
 *   main.rs                — hyper server setup
 *   config.rs              — Config struct, Deserialize, load_from_file
 *   rewriter.rs            — rewrite(path) → String
 *   proxy.rs               — forward() using reqwest::Client
 *   logger.rs              — log_request, log_response with Instant timing
 *
 * Key learning:
 *   - Go: net/http.Client for outbound requests, httputil.ReverseProxy, Header manipulation
 *   - Rust: reqwest for outbound HTTP, hyper for inbound, header copying, async forwarding
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Config config = new Config();

        for (String arg : args) {
            if (arg.startsWith("--config=")) {
                config = Config.loadFromFile(arg.split("=", 2)[1]);
            } else if (arg.startsWith("--port=")) {
                config.setPort(Integer.parseInt(arg.split("=")[1]));
            } else if (arg.startsWith("--target=")) {
                config.setTargetHost(arg.split("=", 2)[1]);
            }
        }

        Logger logger = new Logger(config.isLogRequests());
        Rewriter rewriter = new Rewriter(config);
        Proxy proxy = new Proxy(config, rewriter, logger);

        HttpServer server = HttpServer.create(new InetSocketAddress(config.getPort()), 0);
        server.createContext("/", exchange -> {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().toString();
            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
            Map<String, String> headers = new HashMap<>();
            exchange.getRequestHeaders().forEach((k, v) -> headers.put(k, v.get(0)));
            byte[] body = exchange.getRequestBody().readAllBytes();

            Proxy.ProxyResponse response = proxy.forward(method, path, headers, body, clientIp);

            exchange.sendResponseHeaders(response.statusCode(), response.body().length);
            exchange.getResponseBody().write(response.body());
            exchange.getResponseBody().close();
        });
        server.start();

        System.out.printf("Proxy running on :%d → %s%n", config.getPort(), config.getTargetHost());
    }
}
