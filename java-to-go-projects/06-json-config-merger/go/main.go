package jsonconfigmerger
package main

import (
	"encoding/json"
	"fmt"
	"os"
)

// loadFile reads and parses a JSON file into a generic interface value.
func loadFile(filename string) (interface{}, error) {
	panic("todo")
}

// merge deep-merges two JSON values.
// Objects: merge recursively (override keys win).
// Arrays: replaced entirely.
// Scalars: override replaces base.
// Null in override removes the key.
func merge(base, override interface{}) interface{} {
	panic("todo")
}

// prettyPrint returns an indented JSON string.
func prettyPrint(value interface{}) (string, error) {
	_ = json.MarshalIndent
	panic("todo")
}

func main() {
	if len(os.Args) < 3 {
		fmt.Fprintf(os.Stderr, "Usage: %s <base.json> <override1.json> [override2.json ...]\n", os.Args[0])
		os.Exit(1)
	}

	panic("todo: load files, merge left-to-right, pretty-print result")
}
