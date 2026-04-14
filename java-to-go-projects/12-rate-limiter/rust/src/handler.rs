use std::sync::{Arc, Mutex};
use crate::middleware::{Middleware, RateLimitResult};

/// Extract client IP from request (peer addr).
/// Check rate limit via middleware.
/// Return 200 with JSON body if allowed, 429 with rate-limit headers if denied.
pub async fn handle_request(
    middleware: Arc<Mutex<Middleware>>,
    client_ip: String,
) -> (u16, String, Vec<(String, String)>) {
    todo!()
}

/// Build rate-limit response headers from RateLimitResult.
pub fn rate_limit_headers(result: &RateLimitResult) -> Vec<(String, String)> {
    todo!()
}
