package main

import (
	"bufio"
	"fmt"
	"net"
	"os"
	"sync"
	"time"
)

type Message struct {
	From      string
	Room      string
	Content   string
	Timestamp time.Time
}

func (m Message) String() string {
	return fmt.Sprintf("[%s] %s: %s", m.Timestamp.Format("15:04:05"), m.From, m.Content)
}

type Room struct {
	Name    string
	Members map[string]net.Conn
	History []Message
	mu      sync.RWMutex
}

// newRoom creates a new chat room.
func newRoom(name string) *Room {
	panic("todo")
}

// join adds a client to the room.
func (r *Room) join(nick string, conn net.Conn) {
	panic("todo")
}

// leave removes a client from the room.
func (r *Room) leave(nick string) {
	panic("todo")
}

// broadcast sends a message to all members (except sender).
func (r *Room) broadcast(msg Message) {
	panic("todo")
}

// who returns a list of nicknames in the room.
func (r *Room) who() []string {
	panic("todo")
}

// Server holds all rooms and connected clients.
type Server struct {
	Rooms   map[string]*Room
	Clients map[string]net.Conn // nick -> conn
	mu      sync.RWMutex
}

// newServer creates a new chat server.
func newServer() *Server {
	panic("todo")
}

// handleClient reads commands from a client connection.
// Commands: /nick <name>, /join <room>, /leave, /rooms, /who, /quit
func (s *Server) handleClient(conn net.Conn) {
	_ = bufio.NewScanner(conn)
	panic("todo")
}

// start listens on the given address and accepts connections.
func (s *Server) start(addr string) error {
	_ = net.Listen
	panic("todo")
}

func main() {
	port := "9000"
	if len(os.Args) > 1 {
		port = os.Args[1]
	}

	fmt.Printf("Chat server starting on :%s\n", port)
	panic("todo: create server, start listening")
}
