use std::time::Duration;

/// Wait for Ctrl+C signal using tokio::signal::ctrl_c().
pub async fn shutdown_signal() {
    todo!()
}

/// Run cleanup hooks with a timeout. Hooks are async closures.
pub async fn run_cleanup_hooks(
    hooks: Vec<Box<dyn Fn() + Send + Sync>>,
    timeout: Duration,
) {
    todo!()
}
