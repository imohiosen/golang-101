package downloader;

import java.nio.file.*;
import java.util.*;

/**
 * CLI entry point for the concurrent downloader.
 *
 * Usage: java Main <urls-file> [--concurrency=4] [--output=downloads]
 *
 * Go structure:
 *   main.go                  — CLI arg parsing, orchestration
 *   downloader/pool.go       — DownloadAll with goroutines + semaphore channel
 *   downloader/fetcher.go    — Fetch single URL with net/http
 *   downloader/result.go     — DownloadResult struct
 *   downloader/progress.go   — Thread-safe progress tracker
 *
 * Rust structure:
 *   main.rs                  — CLI parsing with std::env
 *   pool.rs                  — download_all with tokio::spawn + Semaphore
 *   fetcher.rs               — fetch using reqwest
 *   result.rs                — DownloadResult struct + Display
 *   progress.rs              — Arc<Mutex<Progress>> for thread-safe tracking
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String urlsFile = "urls.txt";
        int concurrency = 4;
        String outputDir = "downloads";

        for (String arg : args) {
            if (arg.startsWith("--concurrency=")) {
                concurrency = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.startsWith("--output=")) {
                outputDir = arg.split("=")[1];
            } else {
                urlsFile = arg;
            }
        }

        // Read URLs from file
        List<String> urls = Files.readAllLines(Path.of(urlsFile));
        urls.removeIf(String::isBlank);

        // Create output directory
        Files.createDirectories(Path.of(outputDir));

        System.out.printf("Downloading %d URLs (concurrency: %d)%n", urls.size(), concurrency);

        Fetcher fetcher = new Fetcher(outputDir);
        Pool pool = new Pool(concurrency, fetcher);
        Progress progress = new Progress(urls.size());

        List<DownloadResult> results = pool.downloadAll(urls);
        progress.printSummary(results);
    }
}
