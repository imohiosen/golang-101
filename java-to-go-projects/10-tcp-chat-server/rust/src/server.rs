use crate::room::Room;
use std::collections::HashMap;
use std::sync::Arc;
use tokio::sync::RwLock;
use tokio::net::TcpListener;

/// Start the TCP chat server: bind, accept loop, spawn per client.
pub async fn run(addr: &str) -> Result<(), String> {
    todo!()
}
