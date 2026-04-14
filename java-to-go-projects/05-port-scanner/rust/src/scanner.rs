use crate::result::ScanResult;
use std::net::SocketAddr;

/// Scan a single port on the given host. Returns a ScanResult.
pub async fn scan_port(host: &str, port: u16, timeout_ms: u64) -> ScanResult {
    todo!()
}

/// Scan a range of ports concurrently using tokio::spawn + JoinSet.
pub async fn scan_range(host: &str, start: u16, end: u16, concurrency: usize, timeout_ms: u64) -> Vec<ScanResult> {
    todo!()
}
