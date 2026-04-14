package eventemitter
package main

import (
	"fmt"
	"sync"
	"sync/atomic"
)

type Listener struct {
	ID          int
	Callback    func(interface{})
	Once        bool
	InvokeCount atomic.Int64
}

// invoke calls the listener's callback. Returns true if it was a one-shot listener.
func (l *Listener) invoke(data interface{}) bool {
	panic("todo")
}

type Emitter struct {
	listeners map[string][]*Listener
	nextID    int
	mu        sync.RWMutex
}

func newEmitter() *Emitter {
	panic("todo")
}

// on registers a persistent listener. Returns listener ID.
func (e *Emitter) on(event string, callback func(interface{})) int {
	panic("todo")
}

// once registers a one-shot listener. Returns listener ID.
func (e *Emitter) once(event string, callback func(interface{})) int {
	panic("todo")
}

// off removes a specific listener by event name and ID. Returns true if found.
func (e *Emitter) off(event string, id int) bool {
	panic("todo")
}

// offAll removes all listeners for an event.
func (e *Emitter) offAll(event string) {
	panic("todo")
}

// emit fires an event. Invokes all matching listeners + wildcard "*" listeners.
// Removes one-shot listeners after invocation.
func (e *Emitter) emit(event string, data interface{}) {
	panic("todo")
}

// listenerCount returns the number of listeners for a specific event.
func (e *Emitter) listenerCount(event string) int {
	panic("todo")
}

// eventNames returns all event names that have listeners.
func (e *Emitter) eventNames() []string {
	panic("todo")
}

func main() {
	fmt.Println("Event Emitter Demo")
	panic("todo: register persistent, one-shot, and wildcard listeners; emit events; remove listeners; print summary")
}
