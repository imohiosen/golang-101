use std::collections::HashMap;
use std::time::Duration;

use crate::token_bucket::TokenBucket;
use crate::sliding_window::SlidingWindow;

#[derive(Clone, Copy)]
pub enum Algorithm {
    TokenBucket,
    SlidingWindow,
}

pub struct RateLimitResult {
    pub allowed: bool,
    pub limit: u32,
    pub remaining: u32,
    pub retry_after_ms: u64,
}

enum Limiter {
    Bucket(TokenBucket),
    Window(SlidingWindow),
}

pub struct Middleware {
    pub algorithm: Algorithm,
    pub limit: u32,
    pub window: Duration,
    clients: HashMap<String, Limiter>,
}

impl Middleware {
    pub fn new(algorithm: Algorithm, limit: u32, window: Duration) -> Self {
        todo!()
    }

    /// Check rate limit for a client. Creates limiter on first access.
    pub fn check(&mut self, client_id: &str) -> RateLimitResult {
        todo!()
    }
}
