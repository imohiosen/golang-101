use crate::job::{Job, JobResult};
use crate::worker::Worker;
use std::sync::mpsc;

pub struct Pool {
    workers: Vec<Worker>,
    job_tx: Option<mpsc::Sender<Option<Job>>>,
    result_rx: mpsc::Receiver<JobResult>,
}

impl Pool {
    /// Create a pool with `size` worker threads.
    pub fn new(size: usize) -> Self {
        todo!()
    }

    /// Submit a job to the pool.
    pub fn submit(&self, job: Job) {
        todo!()
    }

    /// Collect `count` results (blocks until all available).
    pub fn collect_results(&self, count: usize) -> Vec<JobResult> {
        todo!()
    }

    /// Send poison pills and join all workers.
    pub fn shutdown(&mut self) {
        todo!()
    }
}
