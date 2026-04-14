package httpproxy
package main

import (
	"fmt"
	"net/http"
	"os"
	"time"
)

type RewriteRule struct {
	PathPrefix string
	Target     string
}

type Config struct {
	Port         int
	TargetHost   string
	RewriteRules []RewriteRule
	LogRequests  bool
	TimeoutMs    int
	AddHeaders   map[string]string
}

func newConfig(port int, targetHost string) *Config {
	panic("todo")
}

// rewrite applies rewrite rules to determine the target URL for a request path.
// Falls back to config.TargetHost if no rule matches.
func rewrite(config *Config, path string) string {
	panic("todo")
}

type ProxyResponse struct {
	StatusCode int
	Body       string
	Headers    map[string]string
}

// forward proxies a request to the target backend.
// Copies relevant headers, sends body, returns response or 502 on error.
func forward(client *http.Client, method, targetURL string, headers map[string]string, body string, clientIP string) ProxyResponse {
	panic("todo")
}

// logRequest logs an incoming request (method, path, client IP).
func logRequest(method, path, clientIP string) {
	panic("todo")
}

// logResponse logs a proxied response (status, duration).
func logResponse(status int, start time.Time) {
	panic("todo")
}

// logError logs a proxy error.
func logError(path, errMsg string) {
	panic("todo")
}

// proxyHandler creates an HTTP handler that proxies all requests.
func proxyHandler(config *Config, client *http.Client) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		panic("todo: rewrite URL, forward request, copy response, log")
	}
}

func main() {
	if len(os.Args) < 2 {
		fmt.Println("HTTP Proxy Server")
	}

	panic("todo: parse CLI args (--config, --port, --target), create config, start HTTP server proxying all requests")
}
