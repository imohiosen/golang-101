package portscanner
package main

import (
	"fmt"
	"net"
	"os"
	"sync"
	"time"
)

type ScanResult struct {
	Host      string
	Port      int
	Open      bool
	LatencyMs int64
}

func (r ScanResult) String() string {
	panic("todo")
}

// scanPort probes a single port with the given timeout. Returns a ScanResult.
func scanPort(host string, port int, timeout time.Duration) ScanResult {
	_ = net.Dialer{Timeout: timeout}
	panic("todo")
}

// scanRange scans ports [start, end] concurrently using a semaphore channel.
func scanRange(host string, start, end, concurrency int, timeout time.Duration) []ScanResult {
	var mu sync.Mutex
	var wg sync.WaitGroup
	sem := make(chan struct{}, concurrency)
	var results []ScanResult

	_ = mu
	_ = &wg
	_ = sem
	_ = results

	panic("todo")
}

func main() {
	if len(os.Args) < 3 {
		fmt.Fprintf(os.Stderr, "Usage: %s <host> <start-port> <end-port> [concurrency]\n", os.Args[0])
		os.Exit(1)
	}

	panic("todo: parse args, call scanRange, print open ports")
}
