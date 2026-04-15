package main

import (
	"context"
	"encoding/json"
	"fmt"
	"os"
	"os/signal"
	"syscall"
	"time"
)

// --- Configuration ---

type KafkaConfig struct {
	Brokers []string
	Topic   string
	GroupID string
}

// --- Message Types ---

// FraudEvent represents a CDC-style fraud detection event.
type FraudEvent struct {
	ID        string    `json:"id"`
	UserID    string    `json:"user_id"`
	Amount    float64   `json:"amount"`
	Merchant  string    `json:"merchant"`
	Timestamp time.Time `json:"timestamp"`
	FraudType string    `json:"fraud_type"` // "chargeback", "velocity", "geo_anomaly"
}

// --- Producer ---

// Producer wraps a Kafka producer connection.
// Go lib: github.com/segmentio/kafka-go
type Producer struct {
	config KafkaConfig
	// writer *kafka.Writer
}

// newProducer creates a Kafka producer configured for the given topic.
// Should configure batching, compression (LZ4), and retries.
func newProducer(config KafkaConfig) *Producer {
	panic("todo: create kafka.Writer with LZ4 compression, batch size 100, batch timeout 10ms")
}

// send publishes a single FraudEvent to Kafka.
// Serialize to JSON, use event.ID as the message key for partition affinity.
func (p *Producer) send(ctx context.Context, event FraudEvent) error {
	_ = json.Marshal
	panic("todo: marshal event to JSON, write kafka.Message with Key=event.ID")
}

// sendBatch publishes multiple FraudEvents in a single batch write.
func (p *Producer) sendBatch(ctx context.Context, events []FraudEvent) error {
	panic("todo: build []kafka.Message, call WriteMessages")
}

// close flushes pending messages and closes the producer.
func (p *Producer) close() error {
	panic("todo: close the kafka.Writer")
}

// --- Consumer ---

// Consumer wraps a Kafka consumer group reader.
type Consumer struct {
	config KafkaConfig
	// reader *kafka.Reader
}

// newConsumer creates a consumer that joins the given consumer group.
// Should configure: min/max bytes, commit interval, start offset.
func newConsumer(config KafkaConfig) *Consumer {
	panic("todo: create kafka.Reader with consumer group, StartOffset=LastOffset")
}

// consume reads messages in a loop, calling handler for each deserialized FraudEvent.
// Must handle context cancellation for graceful shutdown.
func (c *Consumer) consume(ctx context.Context, handler func(FraudEvent) error) error {
	panic("todo: loop ReadMessage, unmarshal JSON to FraudEvent, call handler, commit on success")
}

// consumeBatch reads up to maxBatch messages, calls handler with the batch.
// Returns partial results on context cancellation.
func (c *Consumer) consumeBatch(ctx context.Context, maxBatch int, handler func([]FraudEvent) error) error {
	panic("todo: accumulate messages up to maxBatch or timeout, unmarshal batch, call handler")
}

// close commits final offsets and closes the consumer.
func (c *Consumer) close() error {
	panic("todo: close the kafka.Reader")
}

// --- Demo Helpers ---

// generateEvents creates n synthetic FraudEvents for testing.
func generateEvents(n int) []FraudEvent {
	panic("todo: generate events with random amounts, merchants, fraud types")
}

// printEvent formats and prints a FraudEvent to stdout.
func printEvent(event FraudEvent) {
	_ = fmt.Fprintf
	panic("todo: pretty-print event fields")
}

func main() {
	_ = os.Getenv
	_ = signal.Notify
	_ = syscall.SIGTERM

	fmt.Println("Kafka Producer/Consumer Demo")
	fmt.Println("Requires: KAFKA_BROKERS env var (default: localhost:9092)")
	panic("todo: parse config from env, start producer, send 1000 events, start consumer in goroutine, handle SIGINT/SIGTERM for graceful shutdown")
}
