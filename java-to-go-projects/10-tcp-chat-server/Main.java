package chat;

/**
 * Entry point for the TCP chat server.
 *
 * Usage: java Main [port]        (default: 9000)
 *
 * Test with:  telnet localhost 9000   or   nc localhost 9000
 *
 * Go structure:
 *   main.go              — Start server, accept loop
 *   chat/server.go       — Server struct, Accept loop, room management
 *   chat/client.go       — ClientHandler: goroutine per client, readLoop, command dispatch
 *   chat/room.go         — Room: Join/Leave/Broadcast using channels or sync.RWMutex
 *   chat/message.go      — Message struct with Format()
 *
 * Rust structure:
 *   main.rs              — Start server with tokio
 *   server.rs            — TcpListener::bind, accept loop, Arc<Mutex<HashMap<String,Room>>>
 *   client.rs            — handle_client: tokio::spawn per client, BufReader for line reading
 *   room.rs              — Room: join/leave/broadcast, Arc<Mutex<Vec<Sender>>>
 *   message.rs           — Message struct + Display
 *
 * Key learning:
 *   - Go: net.Listener, goroutines, channels for broadcast, sync.RWMutex
 *   - Rust: tokio::net::TcpListener, Arc<Mutex<>>, tokio::sync::broadcast channel
 */
public class Main {

    public static void main(String[] args) throws Exception {
        int port = 9000;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server(port);
        server.start();
    }
}
