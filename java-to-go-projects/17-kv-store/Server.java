package kvstore;

import java.io.*;
import java.net.*;

/**
 * TCP server that accepts client connections and processes KV commands.
 * Each client gets a dedicated thread.
 */
public class Server {

    private final int port;
    private final Store store;
    private final Persistence persistence;

    public Server(int port, Store store, Persistence persistence) {
        this.port = port;
        this.store = store;
        this.persistence = persistence;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("KV Store listening on port " + port);
        System.out.println("Commands: GET <key>, SET <key> <value>, DELETE <key>, KEYS, SAVE, LOAD, QUIT");

        while (!serverSocket.isClosed()) {
            Socket client = serverSocket.accept();
            new Thread(() -> handleClient(client)).start();
        }
    }

    private void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            out.println("Connected to KV Store. Type QUIT to disconnect.");
            String line;

            while ((line = in.readLine()) != null) {
                Command cmd = Command.parse(line);
                String response = execute(cmd);
                out.println(response);

                if (cmd.getType() == Command.Type.QUIT) {
                    break;
                }
            }
        } catch (IOException e) {
            // Client disconnected
        }
    }

    private String execute(Command cmd) {
        return switch (cmd.getType()) {
            case GET -> {
                String val = store.get(cmd.getKey());
                yield val != null ? val : "(nil)";
            }
            case SET -> {
                store.set(cmd.getKey(), cmd.getValue());
                yield "OK";
            }
            case DELETE -> store.delete(cmd.getKey()) ? "(deleted)" : "(nil)";
            case KEYS -> {
                var keys = store.keys();
                yield keys.isEmpty() ? "(empty)" : String.join(", ", keys);
            }
            case SAVE -> {
                try {
                    persistence.save(store);
                    yield "OK (saved)";
                } catch (IOException e) {
                    yield "ERROR: " + e.getMessage();
                }
            }
            case LOAD -> {
                try {
                    persistence.load(store);
                    yield "OK (loaded " + store.size() + " entries)";
                } catch (IOException e) {
                    yield "ERROR: " + e.getMessage();
                }
            }
            case QUIT -> "Bye!";
            case UNKNOWN -> "ERROR: unknown command — " +
                    (cmd.getValue() != null ? cmd.getValue() : cmd.getRaw());
        };
    }
}
