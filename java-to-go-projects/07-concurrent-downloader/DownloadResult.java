package downloader;

/**
 * Holds the result of a single URL download attempt.
 */
public class DownloadResult {
    private final String url;
    private final int statusCode;
    private final long bytesDownloaded;
    private final long durationMs;
    private final String error;
    private final String savedPath;

    public DownloadResult(String url, int statusCode, long bytesDownloaded,
                          long durationMs, String error, String savedPath) {
        this.url = url;
        this.statusCode = statusCode;
        this.bytesDownloaded = bytesDownloaded;
        this.durationMs = durationMs;
        this.error = error;
        this.savedPath = savedPath;
    }

    public boolean isSuccess() { return error == null && statusCode == 200; }
    public String getUrl() { return url; }
    public int getStatusCode() { return statusCode; }
    public long getBytesDownloaded() { return bytesDownloaded; }
    public long getDurationMs() { return durationMs; }
    public String getError() { return error; }
    public String getSavedPath() { return savedPath; }

    @Override
    public String toString() {
        if (isSuccess()) {
            return String.format("[OK]  %s  (%d bytes, %dms) -> %s",
                    url, bytesDownloaded, durationMs, savedPath);
        }
        return String.format("[ERR] %s  (%s)", url, error);
    }
}
