mod aligned;
mod writer;

fn main() {
    println!("Direct I/O + HugePages Benchmark");
    println!("Architecture: Zero-Copy 4KB Aligned, Safe DMA/HugePages");
    todo!("1) Write 256MB with O_DIRECT, 2) Read back + verify, 3) Buffered comparison, 4) Benchmark throughput, 5) HugePages allocation test, 6) Print results")
}
