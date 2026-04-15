package scanner

import (
	"net"
	"sync"
	"time"
)

type Scanner struct {
	Host        string
	TimeoutMs   int
	Concurrency int
}

func New(host string, timeoutMs, concurrency int) *Scanner {
	return &Scanner{Host: host, TimeoutMs: timeoutMs, Concurrency: concurrency}
}

// ScanPort probes a single port with the configured timeout. Returns a ScanResult.
func (s *Scanner) ScanPort(port int) ScanResult {
	addr := ""
	_ = addr
	_ = net.DialTimeout
	_ = time.Duration(s.TimeoutMs) * time.Millisecond
	panic("todo: dial host:port with timeout, measure latency, return ScanResult")
}

// ScanRange scans ports [start, end] concurrently using goroutines + semaphore channel.
// Returns only open ports, sorted by port number.
func (s *Scanner) ScanRange(start, end int) []ScanResult {
	var mu sync.Mutex
	var wg sync.WaitGroup
	sem := make(chan struct{}, s.Concurrency)
	var results []ScanResult

	_ = mu
	_ = &wg
	_ = sem
	_ = results

	panic("todo: fan out goroutines (one per port), collect results, filter open, sort")
}
