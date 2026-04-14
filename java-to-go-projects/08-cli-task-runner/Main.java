package taskrunner;

import java.util.*;

/**
 * CLI entry point for the task runner.
 *
 * Usage:
 *   java Main <task-name>           — Run a specific task (+ its deps)
 *   java Main --all                 — Run all tasks in dependency order
 *   java Main --list                — List all tasks
 *   java Main --file=custom.json <task>  — Use a custom tasks file
 *
 * Go structure:
 *   main.go                   — CLI parsing, orchestration
 *   taskrunner/task.go        — Task struct
 *   taskrunner/parser.go      — ParseFile, Validate, cycle detection
 *   taskrunner/executor.go    — Run (with deps), RunAll, topological sort
 *                                Uses os/exec.Command for shell execution
 *
 * Rust structure:
 *   main.rs                   — CLI parsing
 *   task.rs                   — Task struct, Deserialize
 *   parser.rs                 — parse_file, validate, cycle detection (DFS)
 *   executor.rs               — run, run_all, topo_sort
 *                                Uses std::process::Command for shell execution
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String tasksFile = "tasks.json";
        String targetTask = null;
        boolean runAll = false;
        boolean listOnly = false;

        for (String arg : args) {
            if (arg.startsWith("--file=")) {
                tasksFile = arg.split("=", 2)[1];
            } else if (arg.equals("--all")) {
                runAll = true;
            } else if (arg.equals("--list")) {
                listOnly = true;
            } else {
                targetTask = arg;
            }
        }

        Map<String, Task> tasks = Parser.parseFile(tasksFile);

        if (listOnly) {
            System.out.println("Available tasks:");
            tasks.values().forEach(t ->
                    System.out.printf("  %-15s %s  deps=%s%n",
                            t.getName(), t.getCommand(), t.getDependsOn()));
            return;
        }

        Executor executor = new Executor(tasks);

        if (runAll) {
            executor.runAll();
        } else if (targetTask != null) {
            executor.run(targetTask);
        } else {
            System.err.println("Usage: task-runner <task-name> | --all | --list");
            System.exit(1);
        }
    }
}
