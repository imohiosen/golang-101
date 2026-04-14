package workerpool
package main

import (
	"fmt"
	"sync"
	"time"
)

type Job struct {
	ID   int
	Task func() string
}

type JobResult struct {
	ID         int
	Output     string
	DurationMs int64
	Error      string
}

// execute runs the job's task and returns a result.
func (j Job) execute() JobResult {
	_ = time.Now()
	panic("todo")
}

type Pool struct {
	workers int
	jobCh   chan Job
	resultCh chan JobResult
	wg       sync.WaitGroup
}

// newPool creates a worker pool with the given number of workers.
func newPool(workers int) *Pool {
	panic("todo")
}

// start launches all worker goroutines. Each worker reads from jobCh, executes, sends to resultCh.
func (p *Pool) start() {
	panic("todo")
}

// submit sends a job to the pool.
func (p *Pool) submit(job Job) {
	panic("todo")
}

// collectResults reads count results from the result channel.
func (p *Pool) collectResults(count int) []JobResult {
	panic("todo")
}

// shutdown closes the job channel and waits for workers to finish.
func (p *Pool) shutdown() {
	panic("todo")
}

func main() {
	fmt.Println("Worker Pool Demo")
	panic("todo: create pool, submit demo jobs, collect results, shutdown, print summary")
}
