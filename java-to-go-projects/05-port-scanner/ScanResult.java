package scanner;

/**
 * Result of scanning a single port.
 */
public record ScanResult(String host, int port, boolean open, long latencyMs) {

    @Override
    public String toString() {
        if (open) {
            return String.format("%-5d OPEN  (%dms)", port, latencyMs);
        }
        return String.format("%-5d closed", port);
    }
}
