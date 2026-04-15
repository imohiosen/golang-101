# Java → Go + Rust Conversion Projects

20 progressively challenging projects, each with a Java reference implementation to convert to **Go** and **Rust**. Projects range from basic HTTP servers to concurrent systems and network tools.

---

## Project Structure

Each project folder contains:
- **Java reference** — One or more `.java` files with complete implementations
- **Go scaffold** — `main.go` with struct definitions and function stubs (`panic("todo")`)
- **Rust scaffold** — `rust/src/` with module files and function stubs (`todo!()`)

```
NN-project-name/
├── *.java              # Java reference implementation
├── main.go             # Go scaffold (stdlib only)
└── rust/
    ├── Cargo.toml      # Rust dependencies
    └── src/
        ├── main.rs     # Entry point
        └── *.rs        # Module files with todo!() stubs
```

---

## Concepts You'll Practice

| Java                    | Go Equivalent                         | Rust Equivalent                         |
|-------------------------|---------------------------------------|-----------------------------------------|
| `class` + getters       | `struct` + exported fields            | `struct` + `impl` block                 |
| `HashMap<K,V>`          | `map[K]V`                             | `HashMap<K,V>`                          |
| `List<T>`               | `[]T` (slice)                         | `Vec<T>`                                |
| `Optional`              | `(T, error)` multiple returns         | `Option<T>` / `Result<T,E>`            |
| `ExecutorService`       | goroutines + `sync.WaitGroup`         | `tokio::spawn` / `std::thread`          |
| `synchronized`          | `sync.Mutex` / `sync.RWMutex`        | `Mutex<T>` / `RwLock<T>` / `Arc`       |
| `AtomicInteger`         | `sync/atomic`                         | `std::sync::atomic`                     |
| `BlockingQueue`         | `chan T` (buffered channel)           | `mpsc::channel` / `mpsc::sync_channel` |
| `HttpServer`            | `net/http`                            | `tokio` + `hyper`                       |
| `Socket` / `ServerSocket` | `net.Dial` / `net.Listen`          | `tokio::net::TcpListener`              |

---

## Projects

### 01 – Hello World API ✅
**Type:** HTTP server · **Go:** ✅ · **Rust:** —
Basic HTTP server with `/` and `/hello?name=` endpoints.

### 02 – Calculator API ✅
**Type:** HTTP server · **Go:** ✅ · **Rust:** ✅
Arithmetic endpoints: add, subtract, multiply, divide with query params.

### 03 – Todo List CRUD ✅
**Type:** HTTP server · **Go:** ✅ · **Rust:** ✅
Full CRUD API with in-memory map, JSON body, method routing.

### 04 – Log Parser
**Type:** CLI tool · **Go:** scaffold · **Rust:** ✅
Parse log files with regex. Count by level, filter errors, extract unique paths.
**Key concepts:** regex, file I/O, `HashMap` aggregation

### 05 – Port Scanner
**Type:** CLI tool · **Go:** scaffold · **Rust:** scaffold
Concurrent TCP port scanner with configurable host, port range, and concurrency.
**Key concepts:** goroutines/`tokio::spawn`, `net.DialTimeout`/`TcpStream::connect`, semaphore pattern

### 06 – JSON Config Merger
**Type:** CLI tool · **Go:** scaffold · **Rust:** scaffold
Deep-merge multiple JSON config files left-to-right. Objects merge recursively, arrays replace, null removes.
**Key concepts:** recursive data structures, `serde_json::Value`/`interface{}`, file I/O

### 07 – Concurrent Downloader
**Type:** CLI tool · **Go:** scaffold · **Rust:** scaffold
Download multiple URLs concurrently with a bounded worker pool. Track progress, save to disk.
**Key concepts:** HTTP client, concurrency limiting (semaphore), file I/O, progress tracking

### 08 – CLI Task Runner
**Type:** CLI tool · **Go:** scaffold · **Rust:** scaffold
Parse `tasks.json` with dependencies, validate (cycle detection via DFS), run in topological order.
**Key concepts:** JSON parsing, directed graph, topological sort, `os/exec`/`std::process::Command`

### 09 – Markdown to HTML
**Type:** CLI tool · **Go:** scaffold · **Rust:** scaffold
Tokenize Markdown (headings, lists, code blocks, blockquotes) and render to HTML with inline formatting.
**Key concepts:** lexer/tokenizer pattern, regex, string building, HTML escaping

### 10 – TCP Chat Server
**Type:** TCP server · **Go:** scaffold · **Rust:** scaffold
Multi-room chat over raw TCP. Commands: `/nick`, `/join`, `/leave`, `/rooms`, `/who`, `/quit`.
**Key concepts:** `net.Listen`/`TcpListener`, goroutine-per-client, broadcast, `sync.RWMutex`/`RwLock`

### 11 – Worker Pool
**Type:** Library + demo · **Go:** scaffold · **Rust:** scaffold
Generic thread/goroutine pool: submit jobs, workers pull from queue, collect results, poison-pill shutdown.
**Key concepts:** `chan`/`mpsc`, `sync.WaitGroup`/`JoinHandle`, closure-based jobs

### 12 – Rate Limiter
**Type:** HTTP server · **Go:** scaffold · **Rust:** scaffold
Per-client rate limiting with two algorithms: **Token Bucket** and **Sliding Window**. Returns `429` with `X-RateLimit-*` headers.
**Key concepts:** time-based algorithms, per-client state (`map` + mutex), HTTP middleware

### 13 – Event Emitter
**Type:** Library + demo · **Go:** scaffold · **Rust:** scaffold
Generic pub/sub emitter: `on`, `once`, `off`, `emit` with wildcard `*` support and auto-removal of one-shot listeners.
**Key concepts:** callback patterns, generics/`interface{}`, ID-based listener management

### 14 – HTTP Proxy
**Type:** HTTP server · **Go:** scaffold · **Rust:** scaffold
Reverse proxy with path-prefix rewrite rules, header copying, `X-Forwarded-For`, request/response logging, `502` on error.
**Key concepts:** `net/http.Client`/`reqwest`, header manipulation, URL rewriting, error handling

### 15 – CSV to JSON
**Type:** CLI tool · **Go:** scaffold · **Rust:** scaffold
Parse CSV (quoted fields, custom delimiters), detect value types (int/float/bool/null), output as JSON objects or arrays.
**Key concepts:** stateful line parser, type inference, `serde_json`/`encoding/json`

### 16 – Cron Parser
**Type:** CLI tool · **Go:** scaffold · **Rust:** scaffold
Parse 5-field cron expressions (ranges, steps, lists, `@daily` shortcuts). Expand to sets, calculate next N execution times.
**Key concepts:** string parsing, `HashSet`/`map[int]bool`, date/time iteration

### 17 – KV Store ✅
**Type:** TCP server · **Go:** scaffold · **Rust:** ✅
Redis-like key-value store over TCP. Commands: `GET`, `SET`, `DELETE`, `KEYS`, `SAVE`, `LOAD`, `QUIT`. JSON persistence.
**Key concepts:** TCP protocol, command parsing, `Arc<RwLock<Store>>`/`sync.RWMutex`, file-based persistence

### 18 – Pipeline
**Type:** Library + demo · **Go:** scaffold · **Rust:** scaffold
Multi-stage concurrent data pipeline: source → filter → map → sink. Each stage runs in its own thread/goroutine, connected by channels.
**Key concepts:** channels (`chan`/`mpsc`), close-to-signal-done, `JoinHandle`/`WaitGroup`, stage composition

### 19 – Graceful Server
**Type:** HTTP server · **Go:** scaffold · **Rust:** scaffold
HTTP server with endpoints (`/health`, `/slow`, `/echo`), logger middleware, and graceful shutdown (drain in-flight requests, run cleanup hooks, timeout).
**Key concepts:** `http.Server.Shutdown`/`hyper` graceful, `os/signal`/`tokio::signal`, `context.WithTimeout`

### 20 – Load Balancer
**Type:** HTTP server · **Go:** scaffold · **Rust:** scaffold
HTTP load balancer with strategies (round-robin, least-connections, random), health checking, request forwarding, and `/lb/status` endpoint.
**Key concepts:** `atomic` counters, background health check goroutine/task, strategy pattern, reverse proxying

---

## Systems Library Projects (21–30)

Projects 21–30 are **Go + Rust only** (no Java reference). Each focuses on a specific library/subsystem from a production fraud detection architecture:

```
CDC Ingestion → Kafka → Avro → RocksDB → Arrow → DataFusion → io_uring → NVMe
```

### 21 – Kafka Producer/Consumer
**Type:** Streaming client · **Go:** scaffold · **Rust:** scaffold
Producer with LZ4 batching, consumer group with manual offset commits, FraudEvent messages.
**Go lib:** `segmentio/kafka-go` · **Rust lib:** `rdkafka`
**Requires:** Running Kafka broker (KAFKA_BROKERS env var)

### 22 – Avro Serde + Schema Evolution
**Type:** Serialization library · **Go:** scaffold · **Rust:** scaffold
Avro encode/decode with schema registry, backward-compatible evolution (v1→v2→v3), union types.
**Go lib:** `linkedin/goavro/v2` · **Rust lib:** `apache-avro`

### 23 – RocksDB Storage Engine
**Type:** Embedded storage · **Go:** scaffold · **Rust:** scaffold
Column families, atomic WriteBatch, range/prefix scans, snapshots, compaction, merge-on-read iterator.
**Go lib:** `linxGnu/grocksdb` · **Rust lib:** `rocksdb`

### 24 – Arrow Columnar Analytics
**Type:** Data processing · **Go:** scaffold · **Rust:** scaffold
Load CSV into RecordBatches, compute sum/avg/min/max, filter, group-by, write/read Arrow IPC files.
**Go lib:** `apache/arrow/go` · **Rust lib:** `arrow-rs`

### 25 – DataFusion SQL Queries
**Type:** Query engine · **Go:** scaffold · **Rust:** scaffold
Custom TableProvider (MemoryTable + FileTable), HybridTable merge-on-read, SQL queries via DataFusion.
**Go lib:** `marcboeker/go-duckdb` · **Rust lib:** `datafusion`

### 26 – io_uring File Server
**Type:** Kernel I/O · **Go:** scaffold · **Rust:** scaffold
Raw io_uring: SQE/CQE submission/completion, O_DIRECT reads, 4KB-aligned buffers, async file serving.
**Go:** raw `syscall` (SYS_IO_URING_SETUP/ENTER) · **Rust lib:** `io-uring`

### 27 – CPU Pinning + IO Priority
**Type:** Systems benchmark · **Go:** scaffold · **Rust:** scaffold
CPU topology detection, `sched_setaffinity`, IOPRIO_RT/IDLE, CorePool with architecture's core layout (0-5 RT, 6 batch, 7 freezer).
**Go lib:** `golang.org/x/sys/unix` · **Rust lib:** `core_affinity` + `nix`

### 28 – LZ4 + Arrow IPC Compression
**Type:** Compression pipeline · **Go:** scaffold · **Rust:** scaffold
LZ4 frame compress/decompress, Arrow IPC → LZ4 pipeline (the "background freezer"), throughput benchmarks.
**Go lib:** `pierrec/lz4/v4` + `arrow/go` · **Rust lib:** `lz4_flex` + `arrow-rs`

### 29 – Direct I/O + HugePages
**Type:** Storage I/O · **Go:** scaffold · **Rust:** scaffold
O_DIRECT write/read with 4KB-aligned buffers, HugePages allocation (2MB mmap), benchmark vs buffered I/O.
**Go lib:** `syscall` (O_DIRECT, Mmap) · **Rust lib:** `nix` + `libc`

### 30 – Glommio Thread-per-Core Server
**Type:** TCP server · **Go:** scaffold · **Rust:** scaffold
Thread-per-core TCP echo server. Rust uses Glommio LocalExecutor. Go approximates with SO_REUSEPORT + pinned OS threads.
**Go:** `syscall` SO_REUSEPORT + `unix.SchedSetaffinity` · **Rust lib:** `glommio`

---

## Architecture Mapping

After completing projects 21–30, every component in this architecture has been practiced:

```
[ Kafka ]           → Project 21
[ Schema Registry ] → Project 22
[ RocksDB LSM/WAL ] → Project 23
[ Arrow IPC ]       → Projects 24, 28
[ DataFusion SQL ]  → Project 25
[ io_uring ]        → Project 26
[ CPU Pinning ]     → Project 27
[ LZ4 Compression ] → Project 28
[ O_DIRECT/DMA ]    → Project 29
[ Glommio ]         → Project 30
```

---

## Status Legend

| Symbol | Meaning |
|--------|---------|
| ✅ | Fully implemented |
| scaffold | Struct/function stubs with `panic("todo")` (Go) or `todo!()` (Rust) — ready for you to implement |
| — | Not started / not applicable |

---

## Go Stack

All Go projects use **stdlib only**:
```
net/http, net, encoding/json, sync, time, strings, regexp,
os, os/exec, os/signal, bufio, strconv, fmt, io, context
```

## Rust Stack

Rust dependencies vary per project (see each `Cargo.toml`):
```
Common:  tokio, serde, serde_json
HTTP:    hyper, http-body-util, hyper-util, reqwest
Other:   regex (04, 09)
```

All Rust projects are managed as a workspace — see root `Cargo.toml`.

## Getting Started

```bash
# Run a Go project
cd java-to-go-projects/04-log-parser
go run main.go sample.log

# Run a Rust project
cargo run --package log-parser

# Check all Rust projects compile
cargo check
```
