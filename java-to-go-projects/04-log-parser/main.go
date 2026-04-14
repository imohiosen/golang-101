package logparser
package main

import (
	"fmt"
	"os"
	"regexp"
)

type LogEntry struct {
	Timestamp string
	Level     string
	Path      string
	Message   string
}

// parseFile reads a log file and returns parsed entries.
func parseFile(filename string) []LogEntry {
	panic("todo")
}

// countByLevel returns a map of log level -> count.
func countByLevel(entries []LogEntry) map[string]int {
	panic("todo")
}

// filterByLevel returns entries matching the given level (case-insensitive).
func filterByLevel(entries []LogEntry, level string) []LogEntry {
	panic("todo")
}

// uniquePaths returns the set of unique request paths.
func uniquePaths(entries []LogEntry) []string {
	panic("todo")
}

func main() {
	if len(os.Args) < 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <logfile>\n", os.Args[0])
		os.Exit(1)
	}

	_ = regexp.MustCompile(`\[(.*?)\] (\w+)\s+(\S+) - (.*)`)

	panic("todo: parse file, print counts, errors, unique paths")
}
