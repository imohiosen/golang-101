# Java Spring Boot → Go Conversion Projects

20 progressively challenging Spring Boot projects to convert to Go. Each teaches core Go concepts.

---

## Go Concepts You'll Practice

| Java / Spring           | Go Equivalent                                  |
|-------------------------|------------------------------------------------|
| `@RestController`       | `http.HandleFunc` / `net/http` or Gin/Chi      |
| `@RequestBody`          | `json.NewDecoder(r.Body).Decode(&v)`           |
| `@PathVariable`         | URL parsing / router params                    |
| `@RequestParam`         | `r.URL.Query().Get("param")`                   |
| `HashMap<K,V>`          | `map[K]V`                                      |
| `List<T>`               | `[]T` (slice)                                  |
| `Optional`              | Multiple return values `(T, error)`            |
| `class` + getters       | `struct` + exported fields                     |
| `AtomicLong`            | `sync/atomic` or `sync.Mutex`                  |
| `LocalDateTime.now()`   | `time.Now()`                                   |

---

## Projects (Beginner → Intermediate)

### 01 - Hello World API
**Files:** `HelloWorldApplication.java`
**Concepts:** Basic HTTP server, query params, routing
**Go tip:** Use `net/http` standard library only — no framework needed.

### 02 - Calculator API
**Files:** `CalculatorController.java`
**Concepts:** Query params, arithmetic, error responses
**Go tip:** `strconv.ParseFloat()` to parse query params.

### 03 - Todo List CRUD
**Files:** `Todo.java`, `TodoController.java`
**Concepts:** Full CRUD, in-memory map, JSON body, path variables
**Go tip:** Use a `map[int64]Todo` + `sync.Mutex` for thread safety.

### 04 - Student Grade Calculator
**Files:** `Student.java`, `GradeController.java`
**Concepts:** Structs with methods, slices, computed fields
**Go tip:** Methods on structs: `func (s Student) Average() float64 { ... }`

### 05 - Bank Account
**Files:** `BankAccount.java`, `BankController.java`
**Concepts:** State mutation, validation, error strings
**Go tip:** Return `(result string, err error)` instead of string-only errors.

### 06 - Library Catalog
**Files:** `Book.java`, `LibraryController.java`
**Concepts:** Search/filter with slices, boolean state, query params
**Go tip:** Use `strings.Contains()` for case-insensitive search with `strings.ToLower()`.

### 07 - Weather API (Mock Data)
**Files:** `WeatherController.java`
**Concepts:** Pre-seeded data maps, nested maps, missing key handling
**Go tip:** Use `struct` instead of `map[string]interface{}` for typed weather data.

### 08 - User Auth (Token-based)
**Files:** `User.java`, `AuthController.java`
**Concepts:** Request headers, UUID tokens, session maps, password hashing
**Go tip:** Use `crypto/sha256` for hashing; read headers with `r.Header.Get("Authorization")`.

### 09 - Inventory Manager
**Files:** `Product.java`, `InventoryController.java`
**Concepts:** PATCH operations, computed totals, threshold filtering
**Go tip:** Range over maps: `for id, product := range products { ... }`

### 10 - Blog API
**Files:** `Post.java`, `BlogController.java`
**Concepts:** Nested slices (comments, tags), filter by tag, timestamps
**Go tip:** `time.Time` for timestamps; embed slices in structs.

### 11 - Quiz App
**Files:** `Quiz.java`, `QuizController.java`
**Concepts:** Pre-seeded data, answer checking, filtering by category
**Go tip:** Use `iota` or typed constants for categories.

### 12 - Expense Tracker
**Files:** `Expense.java`, `ExpenseController.java`
**Concepts:** Aggregation/grouping, filter by category, delete by ID
**Go tip:** Build a summary map: `map[string]float64` for per-category totals.

### 13 - Chat Messages
**Files:** `Message.java`, `ChatController.java`
**Concepts:** Multi-field filtering, mark-as-read mutation, conversation threads
**Go tip:** Sort a slice with `sort.Slice(msgs, func(i, j int) bool { ... })`.

### 14 - Product Catalog
**Files:** `Product.java`, `CatalogController.java`
**Concepts:** Multi-param filtering, dynamic sorting, running average for ratings
**Go tip:** Chain filters by appending to a results slice inside a loop.

### 15 - File Uploader
**Files:** `FileMetadata.java`, `FileController.java`
**Concepts:** Multipart form uploads, disk I/O, file registry
**Go tip:** Use `r.FormFile("file")` and `io.Copy()` to save to disk.

### 16 - Email Sender (Simulated)
**Files:** `EmailRequest.java`, `EmailController.java`
**Concepts:** Slice of recipients, bulk operations, audit log
**Go tip:** Validate with early returns; build log entries as structs.

### 17 - Job Scheduler
**Files:** `Job.java`, `SchedulerController.java`
**Concepts:** Enums (iota), status state machine, manual trigger simulation
**Go tip:** Use `const` with `iota` for job status; `time.Now().Add()` for next run.

### 18 - URL Shortener
**Files:** `ShortUrl.java`, `UrlShortenerController.java`
**Concepts:** Code generation, redirect responses, click tracking
**Go tip:** Use `http.Redirect(w, r, url, 302)` for redirects; `math/rand` for code gen.

### 19 - Rate Limiter
**Files:** `RateLimiter.java`, `RateLimiterController.java`
**Concepts:** Sliding window algorithm, per-client state, `sync.Mutex`, 429 responses
**Go tip:** Store timestamps as `[]time.Time`; use `sync.Mutex` to protect the slice.

### 20 - Event Bus (Pub/Sub)
**Files:** `Event.java`, `EventBusController.java`
**Concepts:** Pub/sub pattern, topic routing, concurrent maps, event log
**Go tip:** Use `sync.RWMutex` for subscriber maps; `map[string][]string` for topic→subs.

---

## Recommended Go Stack

```
Standard library only (projects 01–07):
  net/http, encoding/json, sync, time, strings

Add a router for cleaner path params (projects 08+):
  github.com/go-chi/chi/v5   (lightweight, stdlib-compatible)
  OR
  github.com/gin-gonic/gin   (more Spring-like feel)
```

## Getting Started

```bash
# Create a new Go module for a project
cd java-to-go-projects/01-hello-world
go mod init hello-world
touch main.go
```

Each project should result in a single `main.go` (or a small package) that:
1. Starts an HTTP server on port `8080`
2. Implements the same endpoints as the Java version
3. Uses in-memory storage (no database needed)
