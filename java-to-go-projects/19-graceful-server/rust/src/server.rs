use std::net::SocketAddr;

/// Start a hyper HTTP server with graceful shutdown support.
/// Uses tokio::select! to listen for shutdown signal.
pub async fn start(addr: SocketAddr) -> Result<(), Box<dyn std::error::Error>> {
    todo!()
}
