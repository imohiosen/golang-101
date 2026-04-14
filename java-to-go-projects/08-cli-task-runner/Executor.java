package taskrunner;

import java.io.*;
import java.util.*;

/**
 * Executes tasks in dependency order.
 * Resolves the dependency graph (topological sort),
 * then runs each command via ProcessBuilder.
 */
public class Executor {

    private final Map<String, Task> tasks;
    private final Set<String> executed = new HashSet<>();

    public Executor(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Run a named task and all its dependencies (depth-first).
     */
    public void run(String taskName) throws Exception {
        if (!tasks.containsKey(taskName)) {
            throw new IllegalArgumentException("Unknown task: " + taskName);
        }
        runWithDeps(taskName);
    }

    private void runWithDeps(String name) throws Exception {
        if (executed.contains(name)) return;

        Task task = tasks.get(name);

        // Run dependencies first
        for (String dep : task.getDependsOn()) {
            runWithDeps(dep);
        }

        System.out.printf("▶ Running [%s]: %s%n", name, task.getCommand());
        long start = System.currentTimeMillis();

        ProcessBuilder pb = new ProcessBuilder("sh", "-c", task.getCommand());
        pb.inheritIO();
        if (task.getWorkDir() != null) {
            pb.directory(new File(task.getWorkDir()));
        }
        task.getEnv().forEach((k, v) -> pb.environment().put(k, v));

        Process process = pb.start();
        int exitCode = process.waitFor();
        long elapsed = System.currentTimeMillis() - start;

        if (exitCode != 0) {
            throw new RuntimeException(
                    String.format("Task '%s' failed with exit code %d", name, exitCode));
        }

        System.out.printf("✓ [%s] completed in %dms%n%n", name, elapsed);
        executed.add(name);
    }

    /**
     * Run all tasks in dependency order.
     */
    public void runAll() throws Exception {
        // Topological sort
        List<String> order = topologicalSort();
        for (String name : order) {
            run(name);
        }
    }

    private List<String> topologicalSort() {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (String name : tasks.keySet()) {
            topoVisit(name, visited, result);
        }
        return result;
    }

    private void topoVisit(String name, Set<String> visited, List<String> result) {
        if (visited.contains(name)) return;
        visited.add(name);
        for (String dep : tasks.get(name).getDependsOn()) {
            topoVisit(dep, visited, result);
        }
        result.add(name);
    }
}
