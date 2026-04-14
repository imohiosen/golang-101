package chat;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * TCP chat server that accepts client connections and manages rooms.
 * Each client gets a dedicated thread via ClientHandler.
 */
public class Server {

    private final int port;
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Chat server listening on port " + port);

        // Create a default room
        rooms.put("general", new Room("general"));

        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New connection from " + clientSocket.getRemoteSocketAddress());

            ClientHandler handler = new ClientHandler(clientSocket, this);
            clients.add(handler);
            new Thread(handler).start();
        }
    }

    public Room getOrCreateRoom(String name) {
        return rooms.computeIfAbsent(name, Room::new);
    }

    public List<String> listRoomNames() {
        return rooms.keySet().stream().sorted().toList();
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void stop() throws IOException {
        for (ClientHandler client : clients) {
            client.send("Server shutting down...");
        }
        serverSocket.close();
    }
}
