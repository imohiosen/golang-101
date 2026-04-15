mod pinning;
mod bench;

fn main() {
    println!("CPU Pinning + IO Priority Benchmark");
    println!("Architecture: CORES 0-5 (RT) | CORE 6 (batch) | CORE 7 (freezer)");
    todo!("1) Detect topology, 2) benchmark pinned vs unpinned, 3) IOPRIO_RT vs IDLE, 4) CorePool dispatch, 5) verify core assignment, 6) print results")
}
