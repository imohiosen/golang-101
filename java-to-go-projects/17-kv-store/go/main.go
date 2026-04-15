package kvstore
package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"net"
	"os"
	"strings"
	"sync"
)

type Command int

const (
	CmdGet Command = iota
	CmdSet
	CmdDelete
	CmdKeys
	CmdSave
	CmdLoad
	CmdQuit
	CmdUnknown
)

type ParsedCommand struct {
	Cmd   Command
	Key   string
	Value string
}

// parseCommand parses a raw input line into a ParsedCommand.
func parseCommand(line string) ParsedCommand {
	_ = strings.SplitN(line, " ", 3)
	panic("todo")
}

type Store struct {
	data map[string]string
	mu   sync.RWMutex
}

func newStore() *Store {
	panic("todo")
}

func (s *Store) get(key string) (string, bool) {
	panic("todo")
}

func (s *Store) set(key, value string) {
	panic("todo")
}

func (s *Store) delete(key string) bool {
	panic("todo")
}

func (s *Store) keys() []string {
	panic("todo")
}

func (s *Store) size() int {
	panic("todo")
}

// snapshot returns a copy of all data (for persistence).
func (s *Store) snapshot() map[string]string {
	panic("todo")
}

// replaceAll replaces all data with the given map (for loading).
func (s *Store) replaceAll(data map[string]string) {
	panic("todo")
}

// save writes the store snapshot to a JSON file.
func save(data map[string]string, filename string) error {
	_ = json.Marshal
	_ = os.WriteFile
	panic("todo")
}

// load reads a JSON file and returns the data map.
func load(filename string) (map[string]string, error) {
	_ = os.ReadFile
	_ = json.Unmarshal
	panic("todo")
}

// handleClient reads commands from a client and executes them against the store.
func handleClient(conn net.Conn, store *Store, dataFile string) {
	_ = bufio.NewScanner(conn)
	panic("todo")
}

func main() {
	port := "6379"
	dataFile := "data.json"

	if len(os.Args) > 1 {
		port = os.Args[1]
	}

	fmt.Printf("KV Store starting on :%s\n", port)
	panic("todo: create store, auto-load from dataFile, listen for TCP connections, handle clients in goroutines")
}
