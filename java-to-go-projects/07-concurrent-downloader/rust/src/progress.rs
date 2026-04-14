use std::sync::{Arc, Mutex};

pub struct Progress {
    pub total: usize,
    pub completed: usize,
    pub failed: usize,
}

impl Progress {
    pub fn new(total: usize) -> Self {
        Progress { total, completed: 0, failed: 0 }
    }

    pub fn record_success(&mut self) {
        todo!()
    }

    pub fn record_failure(&mut self) {
        todo!()
    }

    pub fn print_summary(&self) {
        todo!()
    }
}
