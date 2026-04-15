use crate::producer::FraudEvent;
use rdkafka::consumer::{Consumer as RdConsumer, StreamConsumer};
use rdkafka::ClientConfig;
use rdkafka::Message;
use tokio::sync::mpsc;

/// Kafka consumer wrapper using rdkafka's StreamConsumer.
pub struct Consumer {
    // consumer: StreamConsumer,
    topic: String,
    group_id: String,
}

impl Consumer {
    /// Create a new consumer that joins the given consumer group.
    /// Configure: auto.offset.reset=latest, enable.auto.commit=false.
    pub fn new(brokers: &str, topic: &str, group_id: &str) -> Self {
        let _ = ClientConfig::new();
        todo!("Configure StreamConsumer with group.id, subscribe to topic")
    }

    /// Consume messages in a loop, sending deserialized FraudEvents to the channel.
    /// Exits when the cancellation token is triggered.
    pub async fn consume(
        &self,
        tx: mpsc::Sender<FraudEvent>,
        cancel: tokio::sync::watch::Receiver<bool>,
    ) -> Result<(), Box<dyn std::error::Error>> {
        let _ = Message::payload;
        todo!("Loop: recv message from stream, deserialize JSON payload to FraudEvent, send to channel, manually commit offset")
    }

    /// Consume up to max_batch messages or until timeout, return as Vec.
    pub async fn consume_batch(
        &self,
        max_batch: usize,
        timeout: std::time::Duration,
    ) -> Result<Vec<FraudEvent>, Box<dyn std::error::Error>> {
        todo!("Accumulate messages up to max_batch or timeout, deserialize, return batch")
    }
}
