use std::time::Instant;

/// Log an incoming request (method, path, client IP).
pub fn log_request(method: &str, path: &str, client_ip: &str) {
    todo!()
}

/// Log a proxied response (status, duration).
pub fn log_response(status: u16, duration: &Instant) {
    todo!()
}

/// Log a proxy error.
pub fn log_error(path: &str, error: &str) {
    todo!()
}
