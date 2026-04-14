use std::thread::JoinHandle;

pub struct Pipeline {
    handles: Vec<(String, JoinHandle<()>)>,
}

impl Pipeline {
    pub fn new() -> Self {
        todo!()
    }

    /// Add a named stage (already spawned as a JoinHandle).
    pub fn add_stage(&mut self, name: &str, handle: JoinHandle<()>) {
        todo!()
    }

    /// Join all stage threads, waiting for pipeline completion.
    pub fn run(self) {
        todo!()
    }
}
