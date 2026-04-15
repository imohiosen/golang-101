use glommio::prelude::*;
use glommio::net::TcpListener;
use std::sync::Arc;
use std::sync::atomic::{AtomicU64, Ordering};

/// Per-core TCP service using Glommio's thread-per-core model.
/// Each core runs its own LocalExecutor with its own event loop — no cross-core locking.
pub struct CoreService {
    core_id: usize,
    addr: String,
}

impl CoreService {
    /// Create a service bound to a specific core.
    pub fn new(core_id: usize, addr: &str) -> Self {
        todo!("Store core_id and addr")
    }

    /// Run the service on this core's LocalExecutor.
    /// Creates a TcpListener, accepts connections, handles each with the provided handler.
    pub fn run<F>(self, handler: F) where F: Fn(glommio::net::TcpStream) + 'static {
        let _ = LocalExecutorBuilder::new;
        let _ = TcpListener::bind;
        todo!(
            "Create LocalExecutor pinned to core_id,
             run async block: {
                 bind TcpListener,
                 loop: accept connection, spawn_local(handler(stream))
             }"
        )
    }
}

/// Thread-per-core server: spawns one CoreService per available CPU core.
pub struct TPCServer {
    addr: String,
    num_cores: usize,
    handles: Vec<std::thread::JoinHandle<()>>,
}

impl TPCServer {
    /// Create server configured for all available CPU cores.
    pub fn new(addr: &str) -> Self {
        todo!("Detect num_cores, store addr, empty handles vec")
    }

    /// Start all per-core services with the given connection handler.
    pub fn start<F>(&mut self, handler_factory: F)
    where F: Fn(usize) -> Box<dyn Fn(glommio::net::TcpStream) + 'static> + Send + 'static
    {
        todo!(
            "For each core 0..num_cores:
             spawn std::thread, create CoreService(core_id, addr), call run(handler_factory(core_id)),
             store JoinHandle"
        )
    }

    /// Wait for all cores to finish (blocks).
    pub fn join(self) {
        todo!("Join all thread handles")
    }
}

/// Per-core statistics (lock-free — each core only writes its own stats).
pub struct CoreStats {
    pub core_id: usize,
    pub connections: AtomicU64,
    pub bytes_read: AtomicU64,
    pub bytes_written: AtomicU64,
}

impl CoreStats {
    pub fn new(core_id: usize) -> Self {
        todo!("Initialize all atomics to 0")
    }

    pub fn record_connection(&self) {
        todo!("Increment connections with Relaxed ordering (single-writer, no contention)")
    }

    pub fn record_io(&self, read: u64, written: u64) {
        todo!("Add to bytes_read and bytes_written")
    }
}
