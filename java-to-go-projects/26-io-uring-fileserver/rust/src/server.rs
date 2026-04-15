use crate::ring::{Ring, Completion, aligned_alloc};
use nix::fcntl::{open, OFlag};
use nix::sys::stat::Mode;
use std::collections::HashMap;
use std::os::unix::io::RawFd;

/// Async file server using io_uring for O_DIRECT reads.
/// Maps to the FAST PATH EXEC and BATCH PATH EXEC layers of the architecture.
pub struct FileServer {
    ring: Ring,
    data_dir: String,
    inflight: HashMap<u64, PendingRead>,
}

struct PendingRead {
    path: String,
    fd: RawFd,
    buf: Vec<u8>,
    offset: u64,
}

/// Result of a completed file read.
pub struct ReadResult {
    pub id: u64,
    pub path: String,
    pub data: Vec<u8>,
    pub bytes_read: usize,
}

impl FileServer {
    /// Create a file server with io_uring backend.
    pub fn new(data_dir: &str, queue_depth: u32) -> Result<Self, Box<dyn std::error::Error>> {
        todo!("Create Ring, initialize inflight map")
    }

    /// Submit an async O_DIRECT read for the file at path + offset.
    /// Returns a request ID for tracking.
    pub fn submit_read(
        &mut self,
        path: &str,
        offset: u64,
        size: usize,
    ) -> Result<u64, Box<dyn std::error::Error>> {
        let _ = open;
        let _ = OFlag::O_RDONLY | OFlag::O_DIRECT;
        let _ = Mode::empty();
        let _ = aligned_alloc;
        todo!("Open file with O_DIRECT, alloc aligned buf, prepare_read on ring, track in inflight, return ID")
    }

    /// Poll for completed reads. Non-blocking.
    pub fn poll(&mut self) -> Vec<ReadResult> {
        todo!("Reap CQEs from ring, match user_data to inflight, build ReadResult, close fds")
    }

    /// Submit all pending and wait for all inflight to complete.
    pub fn flush(&mut self) -> Vec<ReadResult> {
        todo!("Submit, then loop reap_one until inflight is empty")
    }
}
