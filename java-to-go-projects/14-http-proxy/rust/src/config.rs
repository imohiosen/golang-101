use std::collections::HashMap;

pub struct RewriteRule {
    pub path_prefix: String,
    pub target: String,
}

pub struct Config {
    pub port: u16,
    pub target_host: String,
    pub rewrite_rules: Vec<RewriteRule>,
    pub log_requests: bool,
    pub timeout_ms: u64,
    pub add_headers: HashMap<String, String>,
}

impl Config {
    /// Create a default config.
    pub fn new(port: u16, target_host: String) -> Self {
        todo!()
    }

    /// Load config from a JSON file (optional enhancement).
    pub fn load_from_file(filename: &str) -> Result<Self, String> {
        todo!()
    }
}
