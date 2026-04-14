use std::collections::HashMap;

pub struct ForwardResult {
    pub status_code: u16,
    pub body: String,
    pub duration_ms: u64,
    pub error: Option<String>,
}

/// Forward a request to a backend. Returns ForwardResult.
/// Sets X-Forwarded-For header. Returns 502 on error.
pub async fn forward(
    client: &reqwest::Client,
    backend_url: &str,
    method: &str,
    path: &str,
    headers: &HashMap<String, String>,
    body: Option<String>,
    client_ip: &str,
) -> ForwardResult {
    todo!()
}
