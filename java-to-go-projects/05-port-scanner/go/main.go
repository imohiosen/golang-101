package main

import (
	"fmt"
	"os"
	"strconv"
	"time"

	"port-scanner/scanner"
)

func main() {
	if len(os.Args) < 4 {
		fmt.Fprintf(os.Stderr, "Usage: %s <host> <start-port> <end-port> [timeout-ms] [concurrency]\n", os.Args[0])
		os.Exit(1)
	}

	host := os.Args[1]
	startPort, _ := strconv.Atoi(os.Args[2])
	endPort, _ := strconv.Atoi(os.Args[3])

	timeoutMs := 200
	if len(os.Args) > 4 {
		timeoutMs, _ = strconv.Atoi(os.Args[4])
	}
	concurrency := 100
	if len(os.Args) > 5 {
		concurrency, _ = strconv.Atoi(os.Args[5])
	}

	fmt.Printf("Scanning %s ports %d-%d (timeout=%dms, concurrency=%d)\n",
		host, startPort, endPort, timeoutMs, concurrency)

	s := scanner.New(host, timeoutMs, concurrency)

	t0 := time.Now()
	open := s.ScanRange(startPort, endPort)
	elapsed := time.Since(t0).Milliseconds()

	if len(open) == 0 {
		fmt.Println("No open ports found.")
	} else {
		fmt.Println("\nOpen ports:")
		for _, r := range open {
			fmt.Printf("  %s\n", r)
		}
	}

	fmt.Printf("\nScanned %d ports in %dms\n", endPort-startPort+1, elapsed)
}
