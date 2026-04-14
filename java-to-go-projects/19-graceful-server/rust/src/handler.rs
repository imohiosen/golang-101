/// GET /health — return 200 with JSON {"status": "ok"}
pub async fn health() -> (u16, String) {
    todo!()
}

/// GET /slow — simulate slow operation (5s sleep), then 200
pub async fn slow() -> (u16, String) {
    todo!()
}

/// ANY /echo — return method + path as response body
pub async fn echo(method: &str, path: &str) -> (u16, String) {
    todo!()
}

/// Route a request to the appropriate handler.
pub async fn route(method: &str, path: &str) -> (u16, String) {
    todo!()
}
