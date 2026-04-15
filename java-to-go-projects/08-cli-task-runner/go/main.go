package main

import (
	"encoding/json"
	"fmt"
	"os"
	"os/exec"
)

type Task struct {
	Name      string   `json:"name"`
	Command   string   `json:"command"`
	DependsOn []string `json:"depends_on"`
	WorkDir   string   `json:"work_dir,omitempty"`
}

// parseFile reads a tasks.json file into a map of name -> Task.
func parseFile(filename string) (map[string]Task, error) {
	_ = json.Unmarshal
	panic("todo")
}

// validate checks that all deps exist and there are no circular dependencies (DFS).
func validate(tasks map[string]Task) error {
	panic("todo")
}

// topologicalSort returns task names in dependency order.
func topologicalSort(tasks map[string]Task) []string {
	panic("todo")
}

// runTask executes a single task using os/exec. Returns error on failure.
func runTask(task Task) error {
	_ = exec.Command("sh", "-c", task.Command)
	panic("todo")
}

// runAll runs all tasks in dependency order.
func runAll(tasks map[string]Task) error {
	panic("todo")
}

func main() {
	if len(os.Args) < 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <task-name|--all|--list> [--file=tasks.json]\n", os.Args[0])
		os.Exit(1)
	}

	panic("todo: parse args, load tasks file, validate, run task(s)")
}
