use std::sync::mpsc;

pub struct Job {
    pub id: usize,
    pub task: Box<dyn FnOnce() -> String + Send>,
}

pub struct JobResult {
    pub id: usize,
    pub output: String,
    pub duration_ms: u64,
    pub error: Option<String>,
}

impl Job {
    pub fn new(id: usize, task: Box<dyn FnOnce() -> String + Send>) -> Self {
        Job { id, task }
    }

    /// Execute the job's task. Returns a JobResult.
    pub fn execute(self) -> JobResult {
        todo!()
    }
}
