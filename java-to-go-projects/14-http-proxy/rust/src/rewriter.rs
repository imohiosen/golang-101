use crate::config::Config;

/// Rewrite a request path to a target URL based on config rewrite rules.
/// Falls back to config.target_host if no rule matches.
pub fn rewrite(config: &Config, path: &str) -> String {
    todo!()
}
