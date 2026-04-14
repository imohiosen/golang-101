package chat;

import java.io.*;
import java.net.Socket;

/**
 * Handles a single connected client in its own thread.
 * Reads commands from the client and dispatches to the Server.
 *
 * Commands:
 *   /nick <name>     — Change username
 *   /join <room>     — Join a room (creates if new)
 *   /leave           — Leave current room
 *   /rooms           — List available rooms
 *   /who             — List members in current room
 *   /quit            — Disconnect
 *   <text>           — Send message to current room
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private Room currentRoom;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.username = "anon_" + socket.getPort();
    }

    public String getUsername() { return username; }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            send("Welcome! Use /nick <name> to set your name, /join <room> to join a chat.");

            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("/")) {
                    handleCommand(line);
                } else {
                    handleMessage(line);
                }
            }
        } catch (IOException e) {
            // Client disconnected
        } finally {
            disconnect();
        }
    }

    private void handleCommand(String line) {
        String[] parts = line.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String arg = parts.length > 1 ? parts[1] : "";

        switch (cmd) {
            case "/nick" -> {
                String old = username;
                username = arg;
                send("Nick changed: " + old + " → " + username);
            }
            case "/join" -> {
                if (currentRoom != null) currentRoom.leave(this);
                currentRoom = server.getOrCreateRoom(arg);
                currentRoom.join(this);
            }
            case "/leave" -> {
                if (currentRoom != null) {
                    currentRoom.leave(this);
                    currentRoom = null;
                    send("Left the room.");
                }
            }
            case "/rooms" -> send("Rooms: " + server.listRoomNames());
            case "/who" -> {
                if (currentRoom != null) {
                    send("Members: " + currentRoom.getMemberNames());
                } else {
                    send("You're not in a room.");
                }
            }
            case "/quit" -> disconnect();
            default -> send("Unknown command: " + cmd);
        }
    }

    private void handleMessage(String text) {
        if (currentRoom == null) {
            send("Join a room first with /join <room>");
            return;
        }
        currentRoom.broadcast(new Message(username, currentRoom.getName(), text));
    }

    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void disconnect() {
        if (currentRoom != null) {
            currentRoom.leave(this);
        }
        server.removeClient(this);
        try { socket.close(); } catch (IOException ignored) {}
    }
}
