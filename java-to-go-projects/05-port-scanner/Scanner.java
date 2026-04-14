package scanner;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Concurrent TCP port scanner.
 *
 * Scans a range of ports using a fixed thread pool.
 * Each port gets its own task with a configurable timeout.
 */
public class Scanner {

    private final String host;
    private final int timeoutMs;
    private final int poolSize;

    public Scanner(String host, int timeoutMs, int poolSize) {
        this.host = host;
        this.timeoutMs = timeoutMs;
        this.poolSize = poolSize;
    }

    /**
     * Scan a single port. Returns a ScanResult.
     */
    public ScanResult scanPort(int port) {
        long start = System.nanoTime();
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            return new ScanResult(host, port, true, elapsed);
        } catch (IOException e) {
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            return new ScanResult(host, port, false, elapsed);
        }
    }

    /**
     * Scan a range of ports concurrently.
     * Returns only open ports, sorted by port number.
     */
    public List<ScanResult> scanRange(int startPort, int endPort) {
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        List<Future<ScanResult>> futures = new ArrayList<>();

        for (int port = startPort; port <= endPort; port++) {
            final int p = port;
            futures.add(pool.submit(() -> scanPort(p)));
        }

        List<ScanResult> results = new ArrayList<>();
        for (Future<ScanResult> f : futures) {
            try {
                ScanResult result = f.get();
                if (result.open()) {
                    results.add(result);
                }
            } catch (Exception e) {
                // skip failed scans
            }
        }

        pool.shutdown();
        results.sort(Comparator.comparingInt(ScanResult::port));
        return results;
    }
}
