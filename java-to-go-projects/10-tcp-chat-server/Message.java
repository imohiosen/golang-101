package chat;

import java.time.Instant;

/**
 * Represents a single chat message with sender, room, content and timestamp.
 */
public class Message {
    private final String sender;
    private final String room;
    private final String content;
    private final Instant timestamp;

    public Message(String sender, String room, String content) {
        this.sender = sender;
        this.room = room;
        this.content = content;
        this.timestamp = Instant.now();
    }

    public String getSender() { return sender; }
    public String getRoom() { return room; }
    public String getContent() { return content; }
    public Instant getTimestamp() { return timestamp; }

    /**
     * Format for display: [room] sender: content
     */
    public String format() {
        return String.format("[%s] %s: %s", room, sender, content);
    }
}
