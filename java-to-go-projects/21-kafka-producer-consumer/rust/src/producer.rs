use chrono::{DateTime, Utc};
use rdkafka::producer::{FutureProducer, FutureRecord};
use rdkafka::ClientConfig;
use serde::{Deserialize, Serialize};
use std::time::Duration;

/// CDC-style fraud detection event.
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct FraudEvent {
    pub id: String,
    pub user_id: String,
    pub amount: f64,
    pub merchant: String,
    pub timestamp: DateTime<Utc>,
    pub fraud_type: String, // "chargeback", "velocity", "geo_anomaly"
}

/// Kafka producer wrapper using rdkafka's FutureProducer.
pub struct Producer {
    // producer: FutureProducer,
    topic: String,
}

impl Producer {
    /// Create a new producer with LZ4 compression, configured for the given brokers and topic.
    pub fn new(brokers: &str, topic: &str) -> Self {
        let _ = ClientConfig::new();
        todo!("Configure FutureProducer with compression.type=lz4, linger.ms=10, batch.num.messages=100")
    }

    /// Send a single FraudEvent. Uses event.id as the partition key.
    pub async fn send(&self, event: &FraudEvent) -> Result<(), Box<dyn std::error::Error>> {
        let _ = FutureRecord::<str, str>::to;
        let _ = Duration::from_secs;
        todo!("Serialize event to JSON, create FutureRecord with key=event.id, await delivery")
    }

    /// Send a batch of FraudEvents concurrently, collect delivery results.
    pub async fn send_batch(
        &self,
        events: &[FraudEvent],
    ) -> Result<(), Box<dyn std::error::Error>> {
        todo!("Iterate events, spawn send futures, join_all, collect errors")
    }
}

/// Generate n synthetic FraudEvents for testing.
pub fn generate_events(n: usize) -> Vec<FraudEvent> {
    todo!("Generate events with random amounts, merchants, fraud types")
}
