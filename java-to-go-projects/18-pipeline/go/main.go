package main

import (
	"fmt"
	"sync"
)

// --- Channel ---
// In Go, channels are built-in. Use buffered chan T.
// Close the channel to signal completion.

// --- Stages ---

// mapStage reads from in, applies f, sends to out. Closes out when done.
func mapStage(in <-chan int, out chan<- int, f func(int) int, wg *sync.WaitGroup) {
	panic("todo")
}

// filterStage reads from in, sends to out if predicate is true. Closes out when done.
func filterStage(in <-chan int, out chan<- int, predicate func(int) bool, wg *sync.WaitGroup) {
	panic("todo")
}

// sinkStage reads from in and collects all items into results.
func sinkStage(in <-chan int, results *[]int, mu *sync.Mutex, wg *sync.WaitGroup) {
	panic("todo")
}

// fanOut reads from in and sends each item to all output channels. Closes all when done.
func fanOut(in <-chan int, outs []chan<- int, wg *sync.WaitGroup) {
	panic("todo")
}

// --- Pipeline ---

type Pipeline struct {
	wg sync.WaitGroup
}

func newPipeline() *Pipeline {
	panic("todo")
}

// run waits for all stages to complete.
func (p *Pipeline) run() {
	panic("todo")
}

func main() {
	fmt.Println("Pipeline Demo")
	panic("todo: source(1..=20) -> filter(even) -> map(square) -> sink. Print collected results.")
}
