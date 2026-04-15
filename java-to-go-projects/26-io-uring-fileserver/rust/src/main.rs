mod ring;
mod server;

fn main() {
    println!("io_uring File Server Demo (Linux only)");
    println!("Architecture layer: FAST PATH EXEC + BATCH PATH EXEC");
    todo!("1) Create sample files, 2) setup io_uring, 3) submit O_DIRECT reads, 4) reap completions, 5) benchmark vs std::fs::read, 6) print throughput")
}
