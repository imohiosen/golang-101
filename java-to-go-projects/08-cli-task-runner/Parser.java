package taskrunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Parses a tasks.json file into a list of Task objects.
 * Validates task definitions: no circular deps, all deps exist.
 *
 * Example tasks.json:
 * {
 *   "build": { "command": "go build ./...", "dependsOn": ["lint"] },
 *   "lint":  { "command": "golangci-lint run" },
 *   "test":  { "command": "go test ./...", "dependsOn": ["build"] }
 * }
 */
public class Parser {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Parse tasks.json and return a map of name -> Task.
     */
    public static Map<String, Task> parseFile(String filename) throws IOException {
        String content = Files.readString(Path.of(filename));
        Map<String, Map<String, Object>> raw = mapper.readValue(content,
                new TypeReference<>() {});

        Map<String, Task> tasks = new LinkedHashMap<>();

        for (var entry : raw.entrySet()) {
            Task task = new Task();
            task.setName(entry.getKey());
            Map<String, Object> props = entry.getValue();

            task.setCommand((String) props.get("command"));

            if (props.containsKey("dependsOn")) {
                @SuppressWarnings("unchecked")
                List<String> deps = (List<String>) props.get("dependsOn");
                task.setDependsOn(deps);
            }
            if (props.containsKey("workDir")) {
                task.setWorkDir((String) props.get("workDir"));
            }

            tasks.put(task.getName(), task);
        }

        validate(tasks);
        return tasks;
    }

    /**
     * Validate: all deps exist, no circular dependencies.
     */
    private static void validate(Map<String, Task> tasks) {
        for (Task task : tasks.values()) {
            for (String dep : task.getDependsOn()) {
                if (!tasks.containsKey(dep)) {
                    throw new IllegalArgumentException(
                            "Task '" + task.getName() + "' depends on unknown task '" + dep + "'");
                }
            }
        }
        // Cycle detection via DFS
        Set<String> visited = new HashSet<>();
        Set<String> inStack = new HashSet<>();
        for (String name : tasks.keySet()) {
            if (hasCycle(name, tasks, visited, inStack)) {
                throw new IllegalArgumentException("Circular dependency detected involving '" + name + "'");
            }
        }
    }

    private static boolean hasCycle(String name, Map<String, Task> tasks,
                                     Set<String> visited, Set<String> inStack) {
        if (inStack.contains(name)) return true;
        if (visited.contains(name)) return false;

        visited.add(name);
        inStack.add(name);

        for (String dep : tasks.get(name).getDependsOn()) {
            if (hasCycle(dep, tasks, visited, inStack)) return true;
        }

        inStack.remove(name);
        return false;
    }
}
