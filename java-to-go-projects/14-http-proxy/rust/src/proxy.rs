use std::collections::HashMap;

pub struct ProxyResponse {
    pub status_code: u16,
    pub body: String,
    pub headers: HashMap<String, String>,
}

/// Forward a request to the target backend.
/// Copy relevant headers, send body, return response or 502 on error.
pub async fn forward(
    client: &reqwest::Client,
    method: &str,
    target_url: &str,
    headers: &HashMap<String, String>,
    body: Option<String>,
    client_ip: &str,
) -> ProxyResponse {
    todo!()
}
