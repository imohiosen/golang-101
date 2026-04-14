package chat;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A chat room that holds a set of connected clients.
 * Supports broadcasting messages to all clients in the room.
 */
public class Room {
    private final String name;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final List<Message> history = new ArrayList<>();
    private static final int MAX_HISTORY = 50;

    public Room(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void join(ClientHandler client) {
        clients.add(client);
        broadcast(new Message("SERVER", name,
                client.getUsername() + " joined the room"));
    }

    public void leave(ClientHandler client) {
        clients.remove(client);
        broadcast(new Message("SERVER", name,
                client.getUsername() + " left the room"));
    }

    /**
     * Send a message to all clients in this room.
     */
    public void broadcast(Message msg) {
        synchronized (history) {
            history.add(msg);
            if (history.size() > MAX_HISTORY) {
                history.remove(0);
            }
        }
        for (ClientHandler client : clients) {
            client.send(msg.format());
        }
    }

    public List<String> getMemberNames() {
        return clients.stream().map(ClientHandler::getUsername).toList();
    }

    public int size() { return clients.size(); }
}
