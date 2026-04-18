use tokio::{sync::Semaphore, task::JoinSet};

use crate::result::ScanResult;
use std::{net::SocketAddr, sync::Arc};

/// Scan a single port on the given host. Returns a ScanResult.
pub async fn scan_port(host: &str, port: u16, timeout_ms: u64) -> ScanResult {
    let start_time = std::time::Instant::now();
    let addr = SocketAddr::new(host.parse().expect("Invalid host"), port);
    // connect with tokio
    let open =tokio::time::timeout(
        std::time::Duration::from_millis(timeout_ms), 
        tokio::net::TcpStream::connect(addr)
    )
    .await
    .map(|res| res.is_ok())
    .unwrap_or(false);

    let latency_ms = start_time.elapsed().as_millis() as u64;

        
    ScanResult {
        host: host.to_string(),
        port,
        open,
        latency_ms,
    }

}

/// Scan a range of ports concurrently using tokio::spawn + JoinSet.
pub async fn scan_range(host: &str, start: u16, end: u16, concurrency: usize, timeout_ms: u64) -> Vec<ScanResult> {


    
    let mut join_set = JoinSet::new();
    let semaphore = Arc::new(Semaphore::new(concurrency));
    
    for port in start..=end {
        let host = host.to_string();
        let semaphore = semaphore.clone();
        join_set.spawn(async move {
            let _semaphore_permit = semaphore.acquire().await.unwrap();
            scan_port(&host, port, timeout_ms).await
        });
    }
    let mut results = Vec::new();
    while let Some(res) = join_set.join_next().await {
        if let Ok(scan_result) = res {
            results.push(scan_result);
        }
    }
    results
    
}
