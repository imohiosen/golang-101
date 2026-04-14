package merger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.*;

/**
 * Loads JSON files from disk into JsonNode trees.
 */
public class Loader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode loadFile(String filename) throws IOException {
        String content = Files.readString(Path.of(filename));
        return mapper.readTree(content);
    }

    public static String prettyPrint(JsonNode node) throws IOException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }
}
