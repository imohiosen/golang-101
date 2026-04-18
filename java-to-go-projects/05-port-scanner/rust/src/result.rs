use std::fmt;

pub struct ScanResult {
    pub host: String,
    pub port: u16,
    pub open: bool,
    pub latency_ms: u64,
}

impl fmt::Display for ScanResult {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        fmt::write(
            f,
            format_args!(
                "{}:{} is {} (latency: {} ms)",
                self.host,
                self.port,
                if self.open { "open" } else { "closed" },
                self.latency_ms
            )
        )
    }
}
