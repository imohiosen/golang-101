package scanner;

import java.util.List;

/**
 * CLI entry point for the port scanner.
 *
 * Usage: java Main <host> <startPort> <endPort> [timeout] [threads]
 *
 * Go structure:
 *   main.go              — CLI parsing, calls scanner package
 *   scanner/scanner.go   — Scanner struct, ScanRange method (goroutines + WaitGroup)
 *   scanner/result.go    — ScanResult struct
 *
 * Rust structure:
 *   main.rs              — CLI parsing, calls scanner module
 *   scanner.rs           — scan_range (tokio::spawn + JoinSet)
 *   result.rs            — ScanResult struct
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: scanner <host> <startPort> <endPort> [timeoutMs] [threads]");
            System.exit(1);
        }

        String host = args[0];
        int startPort = Integer.parseInt(args[1]);
        int endPort = Integer.parseInt(args[2]);
        int timeout = args.length > 3 ? Integer.parseInt(args[3]) : 200;
        int threads = args.length > 4 ? Integer.parseInt(args[4]) : 100;

        System.out.printf("Scanning %s ports %d-%d (timeout=%dms, threads=%d)%n",
            host, startPort, endPort, timeout, threads);

        Scanner scanner = new Scanner(host, timeout, threads);
        long t0 = System.currentTimeMillis();
        List<ScanResult> open = scanner.scanRange(startPort, endPort);
        long elapsed = System.currentTimeMillis() - t0;

        if (open.isEmpty()) {
            System.out.println("No open ports found.");
        } else {
            System.out.println("\nOpen ports:");
            for (ScanResult r : open) {
                System.out.println("  " + r);
            }
        }

        System.out.printf("%nScanned %d ports in %dms%n", endPort - startPort + 1, elapsed);
    }
}
