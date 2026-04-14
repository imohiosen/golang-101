package taskrunner;

import java.util.*;

/**
 * Represents a single task/command definition.
 * Each task has a name, a shell command, optional dependencies, and optional env vars.
 */
public class Task {
    private String name;
    private String command;
    private List<String> dependsOn;
    private Map<String, String> env;
    private String workDir;

    public Task() {
        this.dependsOn = new ArrayList<>();
        this.env = new HashMap<>();
    }

    public Task(String name, String command) {
        this();
        this.name = name;
        this.command = command;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }
    public List<String> getDependsOn() { return dependsOn; }
    public void setDependsOn(List<String> dependsOn) { this.dependsOn = dependsOn; }
    public Map<String, String> getEnv() { return env; }
    public void setEnv(Map<String, String> env) { this.env = env; }
    public String getWorkDir() { return workDir; }
    public void setWorkDir(String workDir) { this.workDir = workDir; }

    @Override
    public String toString() {
        return String.format("Task{name='%s', cmd='%s', deps=%s}", name, command, dependsOn);
    }
}
