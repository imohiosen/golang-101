package main
package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"sync"
	"sync/atomic"
	"time"
)

type DownloadResult struct {
	URL             string
	StatusCode      int
	BytesDownloaded int64
	DurationMs      int64
	Error           string
	SavedPath       string
}

func (r DownloadResult) IsSuccess() bool {
	return r.Error == "" && r.StatusCode == 200
}

func (r DownloadResult) String() string {
	panic("todo")
}

type Progress struct {
	Total     int64
	Completed int64
	Failed    int64
}

func (p *Progress) RecordSuccess() {
	atomic.AddInt64(&p.Completed, 1)
}

func (p *Progress) RecordFailure() {
	atomic.AddInt64(&p.Failed, 1)
}

func (p *Progress) PrintSummary() {
	panic("todo")
}

// fetch downloads a single URL and saves to outputDir. Returns a DownloadResult.
func fetch(url, outputDir string) DownloadResult {
	_ = http.Get
	_ = io.Copy
	_ = time.Now()
	panic("todo")
}

// downloadAll downloads all URLs concurrently (up to maxConcurrent at a time).
// Uses a semaphore channel for concurrency control.
func downloadAll(urls []string, outputDir string, maxConcurrent int) []DownloadResult {
	var mu sync.Mutex
	var wg sync.WaitGroup
	sem := make(chan struct{}, maxConcurrent)
	var results []DownloadResult

	_ = mu
	_ = &wg
	_ = sem
	_ = results

	panic("todo")
}

func main() {
	if len(os.Args) < 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <urls-file> [--concurrency=N] [--output=dir]\n", os.Args[0])
		os.Exit(1)
	}

	panic("todo: read URLs from file, call downloadAll, print summary")
}
