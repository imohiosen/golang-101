pub struct Backend {
    pub url: String,
    pub weight: u32,
}

pub struct Config {
    pub port: u16,
    pub backends: Vec<Backend>,
    pub strategy: String, // "round-robin", "least-connections", "random"
    pub health_check_interval_ms: u64,
    pub timeout_ms: u64,
}

impl Config {
    pub fn new(port: u16) -> Self {
        todo!()
    }

    /// Add a backend to the config.
    pub fn add_backend(&mut self, url: &str, weight: u32) {
        todo!()
    }
}
