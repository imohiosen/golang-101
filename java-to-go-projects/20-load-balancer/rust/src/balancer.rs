use std::sync::Arc;
use std::sync::atomic::AtomicUsize;
use crate::strategy::{BackendState, Strategy};

/// Handle an incoming proxy request: select backend, forward, add X-Backend header.
pub async fn handle_proxy(
    client: &reqwest::Client,
    backends: &Arc<Vec<BackendState>>,
    strategy: &Strategy,
    round_robin_index: &AtomicUsize,
    method: &str,
    path: &str,
    client_ip: &str,
) -> (u16, String, Vec<(String, String)>) {
    todo!()
}

/// Handle GET /lb/status — return JSON status of all backends.
pub fn handle_status(backends: &[BackendState]) -> String {
    todo!()
}
