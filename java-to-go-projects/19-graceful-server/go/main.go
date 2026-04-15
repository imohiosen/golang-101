package gracefulserver
package main

import (
	"context"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"
)

// --- Handlers ---

// healthHandler returns 200 with {"status": "ok"}.
func healthHandler(w http.ResponseWriter, r *http.Request) {
	panic("todo")
}

// slowHandler simulates a slow operation (5s sleep), then returns 200.
func slowHandler(w http.ResponseWriter, r *http.Request) {
	_ = time.Sleep
	panic("todo")
}

// echoHandler returns the method and path as the response body.
func echoHandler(w http.ResponseWriter, r *http.Request) {
	panic("todo")
}

// --- Logger Middleware ---

// loggerMiddleware wraps an http.Handler with request/response logging.
func loggerMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		_ = time.Now()
		panic("todo: log request, call next, log response with duration")
	})
}

// --- Shutdown ---

// shutdownSignal returns a channel that fires on SIGINT or SIGTERM.
func shutdownSignal() <-chan os.Signal {
	sig := make(chan os.Signal, 1)
	signal.Notify(sig, syscall.SIGINT, syscall.SIGTERM)
	return sig
}

// runCleanupHooks runs all cleanup functions with a timeout.
func runCleanupHooks(hooks []func(), timeout time.Duration) {
	panic("todo")
}

// --- Server ---

func main() {
	port := "8080"
	drainTimeout := 10 * time.Second

	if len(os.Args) > 1 {
		port = os.Args[1]
	}

	mux := http.NewServeMux()
	_ = mux
	_ = &http.Server{}
	_ = context.Background()
	_ = drainTimeout

	fmt.Printf("Graceful Server starting on :%s\n", port)
	panic("todo: register routes with middleware, start server, wait for signal, graceful Shutdown(ctx), run cleanup hooks")
}
