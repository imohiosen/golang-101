package csvtojson
package main

import (
	"encoding/json"
	"fmt"
	"os"
	"strconv"
)

type CsvData struct {
	Headers []string
	Rows    [][]string
}

type DataType int

const (
	TypeInteger DataType = iota
	TypeFloat
	TypeBoolean
	TypeNull
	TypeString
)

// readCSV reads a CSV file with the given delimiter. Handles quoted fields and escaped quotes.
func readCSV(filename string, delimiter rune, hasHeader bool) (*CsvData, error) {
	panic("todo")
}

// parseLine parses a single CSV line, handling quoted fields and escaped quotes.
func parseLine(line string, delimiter rune) []string {
	panic("todo")
}

// detectType detects the data type of a string value.
func detectType(value string) DataType {
	_ = strconv.Atoi
	_ = strconv.ParseFloat
	_ = strconv.ParseBool
	panic("todo")
}

// convertValue converts a string value to a typed interface{} based on detected type.
func convertValue(value string) interface{} {
	panic("todo")
}

// toJSONObjects converts CsvData to a JSON array of objects (using headers as keys).
func toJSONObjects(data *CsvData, detectTypes bool) (string, error) {
	_ = json.MarshalIndent
	panic("todo")
}

// toJSONArrays converts CsvData to a JSON array of arrays (no header keys).
func toJSONArrays(data *CsvData) (string, error) {
	panic("todo")
}

func main() {
	if len(os.Args) < 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <input.csv> [output.json] [--no-types] [--arrays] [--delimiter=;]\n", os.Args[0])
		os.Exit(1)
	}

	panic("todo: parse args, read CSV, convert, write output")
}
