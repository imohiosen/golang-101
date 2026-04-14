use std::collections::HashMap;
use crate::listener::Listener;

pub struct Emitter<T> {
    listeners: HashMap<String, Vec<Listener<T>>>,
    next_id: usize,
}

impl<T> Emitter<T> {
    pub fn new() -> Self {
        todo!()
    }

    /// Register a persistent listener. Returns listener id.
    pub fn on(&mut self, event: &str, callback: Box<dyn Fn(&T) + Send + Sync>) -> usize {
        todo!()
    }

    /// Register a one-shot listener. Returns listener id.
    pub fn once(&mut self, event: &str, callback: Box<dyn Fn(&T) + Send + Sync>) -> usize {
        todo!()
    }

    /// Remove a specific listener by event name and id. Returns true if found.
    pub fn off(&mut self, event: &str, id: usize) -> bool {
        todo!()
    }

    /// Remove all listeners for an event.
    pub fn off_all(&mut self, event: &str) {
        todo!()
    }

    /// Emit an event. Invokes all matching listeners + wildcard "*" listeners.
    /// Removes one-shot listeners after invocation.
    pub fn emit(&mut self, event: &str, data: &T) {
        todo!()
    }

    /// Count listeners for a specific event.
    pub fn listener_count(&self, event: &str) -> usize {
        todo!()
    }

    /// Return all event names that have listeners.
    pub fn event_names(&self) -> Vec<String> {
        todo!()
    }
}
