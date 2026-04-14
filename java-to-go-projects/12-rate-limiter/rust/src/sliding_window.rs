use std::collections::VecDeque;
use std::time::{Duration, Instant};

pub struct SlidingWindow {
    pub max_requests: u32,
    pub window: Duration,
    pub timestamps: VecDeque<Instant>,
}

impl SlidingWindow {
    pub fn new(max_requests: u32, window: Duration) -> Self {
        todo!()
    }

    /// Remove timestamps older than window.
    fn prune(&mut self) {
        todo!()
    }

    /// Try to acquire one slot. Returns true if allowed.
    pub fn try_acquire(&mut self) -> bool {
        todo!()
    }

    /// Remaining requests allowed in current window.
    pub fn remaining(&mut self) -> u32 {
        todo!()
    }

    /// Milliseconds until next slot opens (0 if not rate-limited).
    pub fn retry_after_ms(&mut self) -> u64 {
        todo!()
    }
}
