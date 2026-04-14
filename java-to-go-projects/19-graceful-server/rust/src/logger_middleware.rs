use std::time::Instant;

/// Wrap a handler call with request/response logging.
/// Logs: client IP, method, path, status code, duration.
pub fn log_request(client_ip: &str, method: &str, path: &str) {
    todo!()
}

pub fn log_response(client_ip: &str, method: &str, path: &str, status: u16, start: Instant) {
    todo!()
}
