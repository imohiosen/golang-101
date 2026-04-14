use std::sync::atomic::{AtomicUsize, Ordering};

pub struct Listener<T> {
    pub id: usize,
    pub callback: Box<dyn Fn(&T) + Send + Sync>,
    pub once: bool,
    pub invoke_count: AtomicUsize,
}

impl<T> Listener<T> {
    pub fn new(id: usize, callback: Box<dyn Fn(&T) + Send + Sync>, once: bool) -> Self {
        todo!()
    }

    /// Invoke the callback. Returns true if this was a one-shot listener
    /// (should be removed after invocation).
    pub fn invoke(&self, data: &T) -> bool {
        todo!()
    }

    pub fn id(&self) -> usize {
        self.id
    }

    pub fn is_once(&self) -> bool {
        self.once
    }
}
