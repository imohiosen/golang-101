mod provider;
mod query;

#[tokio::main]
async fn main() {
    println!("DataFusion SQL Query Engine Demo");
    todo!("1) Create MemoryTable with 1000 hot records, 2) Create FileTable from Arrow IPC, 3) Create HybridTable (merge-on-read), 4) Register as DataFusion TableProvider, 5) Run SQL queries, 6) Print results")
}
