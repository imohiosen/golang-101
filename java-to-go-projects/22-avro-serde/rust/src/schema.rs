use apache_avro::Schema;
use std::collections::HashMap;

/// Local schema registry that tracks schema versions per subject
/// and validates backward compatibility on registration.
pub struct SchemaRegistry {
    schemas: HashMap<String, Vec<Schema>>,
}

impl SchemaRegistry {
    /// Create an empty registry.
    pub fn new() -> Self {
        todo!()
    }

    /// Register a new schema version for the given subject.
    /// Validates backward compatibility: new schema can read data written by previous version.
    pub fn register(&mut self, subject: &str, schema_json: &str) -> Result<usize, String> {
        let _ = Schema::parse_str;
        todo!("Parse schema, check backward compatibility with latest version, append to versions list, return version number")
    }

    /// Get the latest schema for a subject.
    pub fn get_latest(&self, subject: &str) -> Option<&Schema> {
        todo!("Return last element of schemas[subject]")
    }
}

/// Check if new_schema can read data written with old_schema.
/// Rules: new fields must have defaults, types cannot change, required fields cannot be removed.
pub fn is_backward_compatible(old_schema: &Schema, new_schema: &Schema) -> bool {
    todo!("Compare record fields: check type compatibility, verify defaults on new fields")
}

/// V1: Original fraud event schema — id (string), amount (double), timestamp (long)
pub fn fraud_event_v1_schema() &'static str {
    todo!("Return JSON schema string for v1")
}

/// V2: Add optional merchant field with default "unknown"
pub fn fraud_event_v2_schema() -> &'static str {
    todo!("Return JSON schema string for v2 — backward compatible")
}

/// V3: Add union type: fraud_type as [\"null\", \"string\"], default null
pub fn fraud_event_v3_schema() -> &'static str {
    todo!("Return JSON schema string for v3 — backward compatible")
}
