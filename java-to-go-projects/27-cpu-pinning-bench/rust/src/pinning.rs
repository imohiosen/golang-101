use core_affinity::CoreId;
use nix::sched::{sched_setaffinity, sched_getaffinity, CpuSet};
use nix::unistd::Pid;

/// CPU topology information.
pub struct CpuInfo {
    pub num_cpus: usize,
    pub core_ids: Vec<CoreId>,
}

/// Detect available CPUs and NUMA topology.
pub fn detect_topology() -> CpuInfo {
    let _ = core_affinity::get_core_ids;
    todo!("Read core IDs, parse /sys/devices/system/node/ for NUMA info")
}

/// Pin the current thread to a specific CPU core.
pub fn pin_to_core(cpu_id: usize) -> Result<(), nix::Error> {
    let _ = CpuSet::new();
    let _ = sched_setaffinity;
    let _ = Pid::from_raw(0);
    todo!("Create CpuSet, set bit for cpu_id, call sched_setaffinity(0, &set)")
}

/// Pin the current thread to a set of CPU cores.
pub fn pin_to_cores(cpu_ids: &[usize]) -> Result<(), nix::Error> {
    todo!("Build CpuSet with multiple bits, sched_setaffinity")
}

/// Get the current CPU affinity mask.
pub fn get_affinity() -> Result<Vec<usize>, nix::Error> {
    let _ = sched_getaffinity;
    todo!("sched_getaffinity(0), iterate bits, return set CPUs")
}

// --- IO Priority ---

const IOPRIO_CLASS_RT: u32 = 1;
const IOPRIO_CLASS_BE: u32 = 2;
const IOPRIO_CLASS_IDLE: u32 = 3;

/// Set I/O scheduling class and priority for the current thread.
/// class: RT(0-7), BE(0-7), IDLE
pub fn set_io_priority(class: u32, level: u32) -> Result<(), std::io::Error> {
    todo!("syscall SYS_IOPRIO_SET, IOPRIO_WHO_PROCESS, pid=0, encode class<<13|level")
}

/// Get current I/O priority (class, level).
pub fn get_io_priority() -> Result<(u32, u32), std::io::Error> {
    todo!("syscall SYS_IOPRIO_GET, decode class and level")
}

/// CorePool assigns work to pinned threads matching the architecture layout.
pub struct CorePool {
    pub fast_cores: Vec<usize>,   // cores 0-5 for RT
    pub batch_core: usize,         // core 6
    pub freezer_core: usize,       // core 7
}

impl CorePool {
    /// Create pool with architecture core assignments.
    pub fn new() -> Self {
        todo!("Detect cores, assign fast=0..N-3, batch=N-2, freezer=N-1")
    }

    /// Spawn work on a fast-path RT core. Pins thread + sets IOPRIO_RT.
    pub fn submit_fast<F: FnOnce() + Send + 'static>(&self, work: F) -> std::thread::JoinHandle<()> {
        todo!("Pick next fast core, spawn thread, pin, set_io_priority(RT, 0), run work")
    }

    /// Spawn work on the batch core with best-effort IO.
    pub fn submit_batch<F: FnOnce() + Send + 'static>(&self, work: F) -> std::thread::JoinHandle<()> {
        todo!("Spawn thread, pin to batch_core, set_io_priority(BE, 4), run work")
    }

    /// Spawn background work on the freezer core with idle IO.
    pub fn submit_freezer<F: FnOnce() + Send + 'static>(&self, work: F) -> std::thread::JoinHandle<()> {
        todo!("Spawn thread, pin to freezer_core, set_io_priority(IDLE, 7), run work")
    }
}
