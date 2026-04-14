package downloader;

import java.util.*;
import java.util.concurrent.*;

/**
 * Manages a fixed-size thread pool that downloads multiple URLs concurrently.
 * Limits concurrent connections to avoid overwhelming the network.
 */
public class Pool {

    private final int maxConcurrent;
    private final Fetcher fetcher;

    public Pool(int maxConcurrent, Fetcher fetcher) {
        this.maxConcurrent = maxConcurrent;
        this.fetcher = fetcher;
    }

    /**
     * Download all URLs concurrently (up to maxConcurrent at a time).
     * Returns results in completion order.
     */
    public List<DownloadResult> downloadAll(List<String> urls) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(maxConcurrent);
        List<Future<DownloadResult>> futures = new ArrayList<>();

        for (String url : urls) {
            futures.add(executor.submit(() -> fetcher.fetch(url)));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);

        List<DownloadResult> results = new ArrayList<>();
        for (Future<DownloadResult> f : futures) {
            try {
                results.add(f.get());
            } catch (ExecutionException e) {
                // Should not happen since Fetcher catches all exceptions
                results.add(new DownloadResult("unknown", 0, 0, 0,
                        e.getMessage(), null));
            }
        }
        return results;
    }
}
