package main

import (
	"encoding/json"
	"fmt"
	"os"
)

// --- Schema Definition ---

// AvroSchema represents a parsed Avro schema with field definitions.
type AvroSchema struct {
	Type      string      `json:"type"`
	Name      string      `json:"name"`
	Namespace string      `json:"namespace"`
	Fields    []AvroField `json:"fields"`
}

// AvroField represents a single field in an Avro record schema.
type AvroField struct {
	Name    string      `json:"name"`
	Type    interface{} `json:"type"` // string, []interface{} (union), or nested schema
	Default interface{} `json:"default,omitempty"`
}

// --- Codec ---

// Codec handles Avro serialization/deserialization for a given schema.
// Go lib: github.com/linkedin/goavro/v2
type Codec struct {
	schema AvroSchema
	// codec *goavro.Codec
}

// newCodec parses an Avro JSON schema string and returns a Codec.
func newCodec(schemaJSON string) (*Codec, error) {
	panic("todo: parse schema JSON, create goavro.Codec")
}

// encode serializes a Go map to Avro binary format.
func (c *Codec) encode(record map[string]interface{}) ([]byte, error) {
	panic("todo: use codec.BinaryFromNative to encode the record")
}

// decode deserializes Avro binary bytes back to a Go map.
func (c *Codec) decode(data []byte) (map[string]interface{}, error) {
	panic("todo: use codec.NativeFromBinary to decode, return map")
}

// --- Schema Evolution ---

// SchemaRegistry is a local registry that validates schema compatibility.
type SchemaRegistry struct {
	schemas map[string][]AvroSchema // subject -> versions
}

// newRegistry creates an empty schema registry.
func newRegistry() *SchemaRegistry {
	panic("todo: initialize with empty schemas map")
}

// register adds a new schema version for the given subject.
// Validates backward compatibility: new schema can read data written by previous version.
func (r *SchemaRegistry) register(subject string, schema AvroSchema) error {
	panic("todo: check backward compatibility — new fields must have defaults, removed fields must have been optional")
}

// getLatest returns the latest schema version for a subject.
func (r *SchemaRegistry) getLatest(subject string) (*AvroSchema, error) {
	panic("todo: return last element of schemas[subject]")
}

// isBackwardCompatible checks if newSchema can read data written with oldSchema.
// Rules: new fields must have defaults, types cannot change, required fields cannot be removed.
func isBackwardCompatible(oldSchema, newSchema AvroSchema) bool {
	panic("todo: iterate fields, check type compatibility, verify defaults on new fields")
}

// --- Schema Evolution Demo Types ---

// V1: Original fraud event schema
// Fields: id (string), amount (double), timestamp (long)
func fraudEventV1Schema() string {
	panic("todo: return JSON schema string for v1")
}

// V2: Add optional merchant field with default
// Fields: id, amount, timestamp, merchant (string, default=\"unknown\")
func fraudEventV2Schema() string {
	panic("todo: return JSON schema string for v2 — backward compatible with v1")
}

// V3: Add union type field
// Fields: id, amount, timestamp, merchant, fraud_type ([\"null\", \"string\"], default=null)
func fraudEventV3Schema() string {
	panic("todo: return JSON schema string for v3 — backward compatible with v2")
}

// --- Cross-version Read ---

// readWithEvolution decodes data written with writerSchema using readerSchema.
// Demonstrates Avro's schema evolution: reader fills defaults for missing fields.
func readWithEvolution(writerSchema, readerSchema string, data []byte) (map[string]interface{}, error) {
	panic("todo: create codec from readerSchema, decode data, fill defaults for missing fields")
}

func main() {
	_ = json.Marshal
	_ = os.Exit
	_ = fmt.Println

	fmt.Println("Avro Serde + Schema Evolution Demo")
	panic("todo: 1) create v1 codec, encode event, 2) evolve to v2, encode with new field, 3) read v1 data with v2 schema (defaults applied), 4) demonstrate v3 union type, 5) show incompatible change rejection")
}
