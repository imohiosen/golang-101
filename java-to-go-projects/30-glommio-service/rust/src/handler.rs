use glommio::net::TcpStream;
use futures_lite::io::{AsyncReadExt, AsyncWriteExt};
use std::sync::Arc;
use crate::service::CoreStats;

/// Echo handler: reads data and writes it back.
/// Designed for benchmarking — pure I/O, no application logic.
pub async fn echo_handler(mut stream: TcpStream, stats: Arc<CoreStats>) {
    let _ = AsyncReadExt::read;
    let _ = AsyncWriteExt::write_all;
    stats.record_connection();
    todo!(
        "Allocate 4KB buffer, loop:
         read into buffer (break on 0 or error),
         write_all back,
         stats.record_io(n, n)"
    )
}

/// Fraud event handler: deserializes incoming fraud events, validates, responds.
/// More realistic workload simulating the architecture's ingestion path.
pub async fn fraud_handler(mut stream: TcpStream, stats: Arc<CoreStats>) {
    todo!(
        "Read length-prefixed JSON messages, deserialize FraudEvent,
         validate amount > 0, respond with 'OK' or 'REJECT',
         track stats"
    )
}

/// Benchmark client: connects N times, sends data, measures throughput.
/// Run this in a separate thread (not on Glommio — it's the load generator).
pub fn run_benchmark(addr: &str, num_conns: usize, duration_secs: u64) {
    todo!(
        "Spawn num_conns std::threads, each:
         connect to addr, loop for duration_secs sending 1KB messages,
         collect total bytes sent/received, print throughput"
    )
}
