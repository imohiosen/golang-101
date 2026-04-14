package cronparser
package main

import (
	"fmt"
	"os"
	"strings"
)

type Schedule struct {
	Minutes     map[int]bool // 0-59
	Hours       map[int]bool // 0-23
	DaysOfMonth map[int]bool // 1-31
	Months      map[int]bool // 1-12
	DaysOfWeek  map[int]bool // 0-6 (Sun=0)
	Original    string
}

func (s Schedule) String() string {
	panic("todo")
}

// parse parses a 5-field cron expression (or named shortcut) into a Schedule.
// Supports: *, ranges (N-M), steps (*/S, N-M/S), lists (N,M,O).
// Named shortcuts: @yearly, @monthly, @weekly, @daily, @hourly.
func parse(expression string) (*Schedule, error) {
	_ = strings.Fields
	panic("todo")
}

// parseField parses a single cron field into a set of values within [min, max].
func parseField(field string, min, max int) (map[int]bool, error) {
	panic("todo")
}

// matches checks if a given date/time matches the schedule.
func matches(s *Schedule, minute, hour, day, month, weekday int) bool {
	panic("todo")
}

// nextN calculates the next N execution times from a starting point.
// Iterates minute-by-minute from start.
func nextN(s *Schedule, year, month, day, hour, minute, count int) [][5]int {
	panic("todo")
}

func main() {
	if len(os.Args) < 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <cron-expression> [--next=5]\n", os.Args[0])
		os.Exit(1)
	}

	panic("todo: parse cron expression, pretty-print schedule, show next N times")
}
