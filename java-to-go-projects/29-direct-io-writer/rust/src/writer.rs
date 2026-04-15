use crate::aligned::AlignedBuffer;
use nix::fcntl::{open, OFlag};
use nix::sys::stat::Mode;
use nix::unistd::{close, pwrite, pread, fsync};
use std::os::unix::io::RawFd;
use std::time::Instant;

/// Direct I/O writer — bypasses kernel page cache.
/// All writes must be 4KB-aligned in offset and size.
pub struct DirectWriter {
    fd: RawFd,
    path: String,
}

impl DirectWriter {
    /// Open a file with O_DIRECT for writing.
    pub fn open(path: &str) -> Result<Self, Box<dyn std::error::Error>> {
        let _ = OFlag::O_WRONLY | OFlag::O_DIRECT | OFlag::O_CREAT | OFlag::O_TRUNC;
        let _ = Mode::from_bits(0o644);
        todo!("Open file with O_DIRECT|O_WRONLY|O_CREAT|O_TRUNC, return DirectWriter")
    }

    /// Write an aligned buffer at the given 4KB-aligned offset.
    pub fn write_at(&self, buf: &AlignedBuffer, offset: i64) -> Result<usize, Box<dyn std::error::Error>> {
        let _ = pwrite;
        todo!("Validate offset and buf.len are 4KB-aligned, pwrite")
    }

    /// Write all data, splitting into 4KB-aligned chunks. Pad final chunk.
    pub fn write_all(&self, data: &[u8]) -> Result<(), Box<dyn std::error::Error>> {
        todo!("Split into 4KB chunks, alloc aligned buffer per chunk, pad last, pwrite each at correct offset")
    }

    /// Flush to disk (fdatasync).
    pub fn sync(&self) -> Result<(), Box<dyn std::error::Error>> {
        let _ = fsync;
        todo!("Call fsync(fd)")
    }
}

impl Drop for DirectWriter {
    fn drop(&mut self) {
        let _ = close(self.fd);
    }
}

/// Direct I/O reader — bypasses page cache.
pub struct DirectReader {
    fd: RawFd,
    pub file_size: u64,
}

impl DirectReader {
    /// Open a file with O_DIRECT for reading.
    pub fn open(path: &str) -> Result<Self, Box<dyn std::error::Error>> {
        let _ = OFlag::O_RDONLY | OFlag::O_DIRECT;
        todo!("Open O_RDONLY|O_DIRECT, stat for file_size, return DirectReader")
    }

    /// Read size bytes at a 4KB-aligned offset.
    pub fn read_at(&self, offset: i64, size: usize) -> Result<AlignedBuffer, Box<dyn std::error::Error>> {
        let _ = pread;
        todo!("Round size up to 4KB, alloc aligned buf, pread, return buffer")
    }

    /// Read the entire file with sequential 4KB O_DIRECT reads.
    pub fn read_all(&self) -> Result<Vec<u8>, Box<dyn std::error::Error>> {
        todo!("Read in 4KB aligned chunks, concatenate, trim to file_size")
    }
}

impl Drop for DirectReader {
    fn drop(&mut self) {
        let _ = close(self.fd);
    }
}

/// Benchmark: O_DIRECT vs buffered I/O.
pub struct BenchResult {
    pub label: String,
    pub write_mbps: f64,
    pub read_mbps: f64,
}

pub fn benchmark_direct_vs_buffered(size_bytes: usize) -> (BenchResult, BenchResult) {
    let _ = Instant::now();
    todo!("Write size_bytes with DirectWriter, time it; write with std::fs::File, time it; read both, compare throughput")
}
