use std::sync::atomic::{AtomicBool, AtomicUsize};

pub struct BackendState {
    pub url: String,
    pub healthy: AtomicBool,
    pub active_connections: AtomicUsize,
    pub total_requests: AtomicUsize,
    pub weight: u32,
}

#[derive(Clone, Copy)]
pub enum Strategy {
    RoundRobin,
    LeastConnections,
    Random,
}

impl Strategy {
    /// Parse strategy from string.
    pub fn from_str(s: &str) -> Result<Self, String> {
        todo!()
    }

    /// Select a backend from the healthy backends.
    /// round_robin_index is used for RoundRobin strategy (caller manages the counter).
    pub fn select<'a>(
        &self,
        backends: &'a [BackendState],
        round_robin_index: &AtomicUsize,
    ) -> Option<&'a BackendState> {
        todo!()
    }
}

impl BackendState {
    pub fn new(url: String, weight: u32) -> Self {
        todo!()
    }

    pub fn increment_connections(&self) {
        todo!()
    }

    pub fn decrement_connections(&self) {
        todo!()
    }
}
