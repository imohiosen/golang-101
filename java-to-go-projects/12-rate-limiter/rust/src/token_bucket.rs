use std::time::Instant;

pub struct TokenBucket {
    pub capacity: u32,
    pub refill_rate: f64, // tokens per second
    pub tokens: f64,
    pub last_refill: Instant,
}

impl TokenBucket {
    pub fn new(capacity: u32, refill_rate: f64) -> Self {
        todo!()
    }

    /// Refill tokens based on elapsed time since last refill.
    fn refill(&mut self) {
        todo!()
    }

    /// Try to acquire one token. Returns true if allowed.
    pub fn try_acquire(&mut self) -> bool {
        todo!()
    }

    /// Return current available tokens (after refill).
    pub fn available_tokens(&mut self) -> u32 {
        todo!()
    }
}
