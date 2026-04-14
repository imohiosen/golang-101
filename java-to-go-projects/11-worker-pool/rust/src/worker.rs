use crate::job::{Job, JobResult};
use std::sync::mpsc;
use std::sync::{Arc, Mutex};

/// A single worker thread that pulls jobs from a shared receiver.
pub struct Worker {
    pub id: usize,
    pub handle: Option<std::thread::JoinHandle<()>>,
}

impl Worker {
    /// Spawn a worker that loops: recv job from rx, execute, send result to result_tx.
    pub fn new(
        id: usize,
        rx: Arc<Mutex<mpsc::Receiver<Option<Job>>>>,
        result_tx: mpsc::Sender<JobResult>,
    ) -> Self {
        todo!()
    }
}
