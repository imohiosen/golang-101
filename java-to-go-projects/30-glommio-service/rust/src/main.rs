mod service;
mod handler;

fn main() {
    println!("Glommio Thread-per-Core TCP Server");
    println!("Architecture: RUST APPS: GLOMMIO (thread-per-core runtime)");
    todo!("1) Detect cores, 2) spawn LocalExecutor per core, 3) bind TCP listener per core, 4) echo handler, 5) benchmark, 6) print per-core stats")
}
