package kvstore;

/**
 * Represents a parsed client command.
 * Supports: GET, SET, DELETE, KEYS, SAVE, LOAD, QUIT
 */
public class Command {

    public enum Type {
        GET, SET, DELETE, KEYS, SAVE, LOAD, QUIT, UNKNOWN
    }

    private final Type type;
    private final String key;
    private final String value;
    private final String raw;

    public Command(Type type, String key, String value, String raw) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.raw = raw;
    }

    public Type getType() { return type; }
    public String getKey() { return key; }
    public String getValue() { return value; }
    public String getRaw() { return raw; }

    /**
     * Parse a raw command string into a Command object.
     *
     * Format: COMMAND key [value]
     * Examples:
     *   SET name Alice
     *   GET name
     *   DELETE name
     *   KEYS
     *   SAVE
     *   LOAD
     *   QUIT
     */
    public static Command parse(String input) {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return new Command(Type.UNKNOWN, null, null, input);
        }

        String[] parts = trimmed.split("\\s+", 3);
        String cmd = parts[0].toUpperCase();

        return switch (cmd) {
            case "GET" -> {
                if (parts.length < 2) yield error("GET requires a key", input);
                yield new Command(Type.GET, parts[1], null, input);
            }
            case "SET" -> {
                if (parts.length < 3) yield error("SET requires key and value", input);
                yield new Command(Type.SET, parts[1], parts[2], input);
            }
            case "DELETE", "DEL" -> {
                if (parts.length < 2) yield error("DELETE requires a key", input);
                yield new Command(Type.DELETE, parts[1], null, input);
            }
            case "KEYS" -> new Command(Type.KEYS, null, null, input);
            case "SAVE" -> new Command(Type.SAVE, null, null, input);
            case "LOAD" -> new Command(Type.LOAD, null, null, input);
            case "QUIT", "EXIT" -> new Command(Type.QUIT, null, null, input);
            default -> new Command(Type.UNKNOWN, null, null, input);
        };
    }

    private static Command error(String msg, String raw) {
        return new Command(Type.UNKNOWN, null, msg, raw);
    }
}
