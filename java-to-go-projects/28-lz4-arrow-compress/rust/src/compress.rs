use lz4_flex::frame::{FrameEncoder, FrameDecoder};
use std::io::{Read, Write};

/// Compress a byte slice using LZ4 frame format.
pub fn compress_bytes(data: &[u8]) -> Result<Vec<u8>, std::io::Error> {
    let _ = FrameEncoder::new;
    todo!("Create FrameEncoder wrapping a Vec<u8>, write data, finish, return compressed bytes")
}

/// Decompress LZ4 frame bytes.
pub fn decompress_bytes(data: &[u8]) -> Result<Vec<u8>, std::io::Error> {
    let _ = FrameDecoder::new;
    todo!("Create FrameDecoder from slice, read_to_end into Vec<u8>, return")
}

/// Compress a file to another file using LZ4 frame format.
pub fn compress_file(src: &str, dst: &str) -> Result<CompressionStats, Box<dyn std::error::Error>> {
    todo!("Open src, create FrameEncoder wrapping dst file, copy, return stats (original size, compressed size, ratio)")
}

/// Decompress an LZ4 file.
pub fn decompress_file(src: &str, dst: &str) -> Result<(), Box<dyn std::error::Error>> {
    todo!("Open src with FrameDecoder, copy to dst file")
}

/// Compression statistics.
pub struct CompressionStats {
    pub original_bytes: u64,
    pub compressed_bytes: u64,
    pub ratio: f64,
}

/// Benchmark compression at different buffer sizes, return throughput stats.
pub fn benchmark(data: &[u8]) -> Vec<BenchResult> {
    todo!("Run compress/decompress with 64KB/256KB/1MB/4MB buffer reads, measure time, compute MB/s")
}

pub struct BenchResult {
    pub label: String,
    pub compress_mbps: f64,
    pub decompress_mbps: f64,
    pub ratio: f64,
}
