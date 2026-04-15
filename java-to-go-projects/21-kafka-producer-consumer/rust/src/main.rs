mod consumer;
mod producer;

fn main() {
    println!("Kafka Producer/Consumer Demo");
    println!("Requires: KAFKA_BROKERS env var (default: localhost:9092)");
    todo!("Parse config from env, produce 1000 FraudEvents, consume in separate task, handle Ctrl+C for graceful shutdown")
}
