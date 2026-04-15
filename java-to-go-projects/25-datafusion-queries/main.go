package datafusionqueries
package main

import (
	"context"
	"database/sql"
	"fmt"
	"os"
)

// --- Custom Table Provider ---

// This project teaches you to build SQL queries against custom data sources.
// Go lib: github.com/marcboeker/go-duckdb (embedded SQL engine, closest Go equiv to DataFusion)
// Architecture mapping: DataFusion SQL ENGINE + HYBRID TABLEPROVIDER

// FraudRecord represents a single row in our custom table.
type FraudRecord struct {
	ID        string
	UserID    string
	Amount    float64
	Merchant  string
	Timestamp int64
	FraudType string
}

// TableSource is an interface for custom data backends that the SQL engine queries.
type TableSource interface {
	// scan returns all rows (full table scan).
	scan() ([]FraudRecord, error)
	// scanRange returns rows within a key range (pushed-down predicate).
	scanRange(startKey, endKey string) ([]FraudRecord, error)
	// schema returns column names and types.
	schema() map[string]string
}

// --- In-Memory Table ---

// MemoryTable stores fraud records in memory — simulates the "hot" RocksDB buffer.
type MemoryTable struct {
	records []FraudRecord
}

func newMemoryTable(records []FraudRecord) *MemoryTable {
	panic("todo: store records, sort by ID for efficient range scans")
}

func (m *MemoryTable) scan() ([]FraudRecord, error) {
	panic("todo: return all records")
}

func (m *MemoryTable) scanRange(startKey, endKey string) ([]FraudRecord, error) {
	panic("todo: binary search for start, iterate until endKey")
}

func (m *MemoryTable) schema() map[string]string {
	panic("todo: return column name -> type map")
}

// --- File Table ---

// FileTable reads fraud records from Arrow IPC files — simulates the "frozen" 53TB layer.
type FileTable struct {
	path string
}

func newFileTable(path string) *FileTable {
	panic("todo: verify file exists, store path")
}

func (f *FileTable) scan() ([]FraudRecord, error) {
	panic("todo: open Arrow IPC file, read all batches, convert to FraudRecord slice")
}

func (f *FileTable) scanRange(startKey, endKey string) ([]FraudRecord, error) {
	panic("todo: read IPC file, filter rows by key range during scan")
}

func (f *FileTable) schema() map[string]string {
	panic("todo: read schema from IPC file header")
}

// --- Hybrid Table (Merge-on-Read) ---

// HybridTable merges results from MemoryTable and FileTable — the UNION ALL pattern.
// This is the core merge-on-read logic from the architecture.
type HybridTable struct {
	hot  *MemoryTable
	cold *FileTable
}

func newHybridTable(hot *MemoryTable, cold *FileTable) *HybridTable {
	panic("todo: store both sources")
}

// scan merges hot and cold data. Hot data takes precedence (newer).
func (h *HybridTable) scan() ([]FraudRecord, error) {
	panic("todo: scan both sources, merge by ID (hot overwrites cold), return sorted")
}

func (h *HybridTable) scanRange(startKey, endKey string) ([]FraudRecord, error) {
	panic("todo: range scan both sources, merge by ID")
}

func (h *HybridTable) schema() map[string]string {
	panic("todo: return schema from hot table")
}

// --- SQL Query Engine ---

// QueryEngine wraps DuckDB for SQL queries against registered tables.
type QueryEngine struct {
	// db *sql.DB (DuckDB connection)
	tables map[string]TableSource
}

// newQueryEngine initializes an in-memory DuckDB instance.
func newQueryEngine() (*QueryEngine, error) {
	_ = sql.Open
	panic("todo: open DuckDB in-memory, create QueryEngine with empty tables map")
}

// registerTable makes a TableSource available under the given SQL table name.
// Loads data from the source into a DuckDB table.
func (q *QueryEngine) registerTable(name string, source TableSource) error {
	panic("todo: scan source, create DuckDB table with schema, insert all rows")
}

// query executes a SQL query and returns results as FraudRecords.
func (q *QueryEngine) query(ctx context.Context, sql string) ([]FraudRecord, error) {
	panic("todo: execute SQL, scan rows into FraudRecord slice")
}

// queryScalar executes a SQL query that returns a single numeric value (e.g., COUNT, SUM).
func (q *QueryEngine) queryScalar(ctx context.Context, sql string) (float64, error) {
	panic("todo: execute SQL, scan single value")
}

// close shuts down the DuckDB connection.
func (q *QueryEngine) close() error {
	panic("todo: close sql.DB")
}

func main() {
	_ = os.Args
	_ = fmt.Println

	fmt.Println("DataFusion-style SQL Query Engine Demo")
	panic(`todo:
1) Create MemoryTable with 1000 "hot" fraud records
2) Create FileTable from Arrow IPC file (write sample data first)
3) Create HybridTable merging hot + cold
4) Register with QueryEngine
5) Run queries:
   - SELECT * FROM fraud WHERE amount > 5000
   - SELECT fraud_type, COUNT(*), AVG(amount) FROM fraud GROUP BY fraud_type
   - SELECT * FROM fraud WHERE id BETWEEN 'F0100' AND 'F0200'
6) Print results, compare merge-on-read correctness`)
}
