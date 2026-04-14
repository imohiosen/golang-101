use serde_json::Value;

/// Deep-merge two JSON values.
/// Objects: merge recursively (override keys win).
/// Arrays: replaced entirely.
/// Scalars: override replaces base.
/// Null in override removes the key.
pub fn merge(base: Value, override_val: Value) -> Value {
    todo!()
}
