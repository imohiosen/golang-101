use std::sync::Arc;
use std::time::Duration;
use crate::strategy::BackendState;

/// Periodically check health of all backends by sending GET /health.
/// Update BackendState.healthy accordingly. Log state transitions.
pub async fn health_check_loop(
    backends: Arc<Vec<BackendState>>,
    interval: Duration,
    client: reqwest::Client,
) {
    todo!()
}

/// Check a single backend's health.
async fn check_health(client: &reqwest::Client, url: &str) -> bool {
    todo!()
}
