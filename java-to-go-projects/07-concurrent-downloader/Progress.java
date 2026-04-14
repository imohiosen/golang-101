package downloader;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tracks and displays download progress.
 * Thread-safe — multiple download threads update it concurrently.
 */
public class Progress {

    private final int total;
    private final AtomicInteger completed = new AtomicInteger(0);
    private final AtomicInteger failed = new AtomicInteger(0);

    public Progress(int total) {
        this.total = total;
    }

    public void recordSuccess() {
        int done = completed.incrementAndGet();
        printStatus(done);
    }

    public void recordFailure() {
        failed.incrementAndGet();
        int done = completed.incrementAndGet();
        printStatus(done);
    }

    private void printStatus(int done) {
        System.out.printf("\rProgress: %d/%d (failed: %d)", done, total, failed.get());
    }

    public void printSummary(List<DownloadResult> results) {
        long totalBytes = results.stream()
                .filter(DownloadResult::isSuccess)
                .mapToLong(DownloadResult::getBytesDownloaded)
                .sum();
        long totalMs = results.stream()
                .mapToLong(DownloadResult::getDurationMs)
                .max().orElse(0);

        System.out.println("\n\n=== Summary ===");
        System.out.printf("Total: %d  |  OK: %d  |  Failed: %d%n",
                total, total - failed.get(), failed.get());
        System.out.printf("Bytes: %d  |  Wall time: %dms%n", totalBytes, totalMs);

        results.forEach(r -> System.out.println("  " + r));
    }
}
