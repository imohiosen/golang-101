package kvstore;

/**
 * Entry point: TCP-based key-value store server.
 *
 * Usage: java Main [--port=6379] [--file=data.json]
 *
 * Test with: telnet localhost 6379    then type: SET hello world / GET hello
 *
 * Go structure:
 *   main.go                    — CLI args, start server
 *   kvstore/command.go         — Command struct + Parse(input)
 *   kvstore/store.go           — Store with sync.RWMutex, Get/Set/Delete/Keys
 *   kvstore/persistence.go     — Save/Load using encoding/json + os.WriteFile/ReadFile
 *   kvstore/server.go          — net.Listen, accept loop, goroutine per client
 *                                 bufio.Scanner for reading lines
 *
 * Rust structure:
 *   main.rs                    — CLI args, start server
 *   command.rs                 — Command enum + parse(input)
 *   store.rs                   — Store: Arc<RwLock<HashMap<String,String>>>
 *   persistence.rs             — save/load using serde_json + std::fs
 *   server.rs                  — TcpListener, tokio::spawn per client
 *                                 BufReader + lines() for reading
 *
 * Key learning:
 *   - Go: net.Listener, goroutines, sync.RWMutex, bufio.Scanner, encoding/json
 *   - Rust: TcpListener, Arc<RwLock<>>, tokio::spawn, BufReader::lines()
 */
public class Main {

    public static void main(String[] args) throws Exception {
        int port = 6379;
        String dataFile = "data.json";

        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.startsWith("--file=")) {
                dataFile = arg.split("=", 2)[1];
            }
        }

        Store store = new Store();
        Persistence persistence = new Persistence(dataFile);

        // Auto-load if data file exists
        if (java.nio.file.Files.exists(java.nio.file.Path.of(dataFile))) {
            persistence.load(store);
        }

        Server server = new Server(port, store, persistence);
        server.start();
    }
}
