mod result;
mod scanner;

#[tokio::main]
async fn main() {
    let args: Vec<String> = std::env::args().collect();
    if args.len() != 5 {
        eprintln!("Usage: {} <host> <start_port> <end_port> <concurrency>", args[0]);
        std::process::exit(1);
    }

    let results = scanner::scan_range(
        &args[1],
        args[2].parse().expect("Invalid start port"),
        args[3].parse().expect("Invalid end port"),
        args[4].parse().expect("Invalid concurrency"),
        1000, // timeout in ms
    ).await;
    
    for result in results {
        println!(
            "{}",
            result
        );

    }
}
