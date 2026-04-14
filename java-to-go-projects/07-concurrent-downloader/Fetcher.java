package downloader;

import java.io.*;
import java.net.*;
import java.nio.file.*;

/**
 * Downloads a single URL to a local file.
 * Responsible for HTTP connection, streaming bytes, and measuring timing.
 */
public class Fetcher {

    private final String outputDir;

    public Fetcher(String outputDir) {
        this.outputDir = outputDir;
    }

    /**
     * Fetch a single URL. Returns a DownloadResult with timing and size info.
     */
    public DownloadResult fetch(String urlString) {
        long start = System.currentTimeMillis();
        try {
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(30_000);

            int status = conn.getResponseCode();
            if (status != 200) {
                return new DownloadResult(urlString, status, 0,
                        elapsed(start), "HTTP " + status, null);
            }

            String filename = extractFilename(urlString);
            Path dest = Path.of(outputDir, filename);
            long bytes;
            try (InputStream in = conn.getInputStream();
                 OutputStream out = Files.newOutputStream(dest)) {
                bytes = in.transferTo(out);
            }

            return new DownloadResult(urlString, status, bytes,
                    elapsed(start), null, dest.toString());

        } catch (Exception e) {
            return new DownloadResult(urlString, 0, 0,
                    elapsed(start), e.getMessage(), null);
        }
    }

    private String extractFilename(String url) {
        String path = url.substring(url.lastIndexOf('/') + 1);
        if (path.isEmpty() || path.contains("?")) {
            path = "download_" + System.nanoTime();
        }
        return path;
    }

    private long elapsed(long start) {
        return System.currentTimeMillis() - start;
    }
}
