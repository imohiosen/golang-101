package loadbalancer
package main

import (
	"fmt"
	"math/rand"
	"net/http"
	"os"
	"sync"
	"sync/atomic"
	"time"
)

type Backend struct {
	URL    string
	Weight int
}

type Config struct {
	Port                  int
	Backends              []Backend
	Strategy              string // "round-robin", "least-connections", "random"
	HealthCheckIntervalMs int
	TimeoutMs             int
}

func newConfig(port int) *Config {
	panic("todo")
}

type BackendState struct {
	URL               string
	Healthy           atomic.Bool
	ActiveConnections atomic.Int64
	TotalRequests     atomic.Int64
	Weight            int
}

func newBackendState(url string, weight int) *BackendState {
	panic("todo")
}

// --- Strategy ---

type StrategyType int

const (
	RoundRobin StrategyType = iota
	LeastConnections
	Random
)

func parseStrategy(s string) (StrategyType, error) {
	panic("todo")
}

// selectBackend picks a backend from healthy backends based on strategy.
func selectBackend(strategy StrategyType, backends []*BackendState, rrIndex *atomic.Int64) *BackendState {
	_ = rand.Intn
	panic("todo")
}

// --- Health Checker ---

// healthCheckLoop periodically checks each backend's /health endpoint.
func healthCheckLoop(backends []*BackendState, interval time.Duration, client *http.Client, done <-chan struct{}) {
	panic("todo")
}

func checkHealth(client *http.Client, url string) bool {
	panic("todo")
}

// --- Forwarder ---

type ForwardResult struct {
	StatusCode int
	Body       string
	DurationMs int64
	Error      string
}

// forward proxies a request to a backend. Sets X-Forwarded-For. Returns 502 on error.
func forward(client *http.Client, backendURL, method, path string, headers map[string]string, body string, clientIP string) ForwardResult {
	panic("todo")
}

// --- Balancer Handler ---

// balancerHandler creates an HTTP handler that load-balances requests.
func balancerHandler(backends []*BackendState, strategy StrategyType, rrIndex *atomic.Int64, client *http.Client) http.HandlerFunc {
	var mu sync.Mutex
	_ = mu
	return func(w http.ResponseWriter, r *http.Request) {
		panic("todo: if /lb/status return status JSON, else select backend, forward, add X-Backend header")
	}
}

// statusJSON returns JSON status of all backends.
func statusJSON(backends []*BackendState) string {
	panic("todo")
}

func main() {
	port := "8080"
	if len(os.Args) > 1 {
		port = os.Args[1]
	}

	fmt.Printf("Load Balancer starting on :%s\n", port)
	panic("todo: parse CLI args (--port, --strategy, --backend=url), build config, create BackendStates, spawn health checker, start HTTP server")
}
