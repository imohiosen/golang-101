use serde_json::Value;

#[derive(Debug, PartialEq)]
pub enum DataType {
    Integer,
    Float,
    Boolean,
    Null,
    Str,
}

/// Detect the data type of a string value.
pub fn detect(value: &str) -> DataType {
    todo!()
}

/// Convert a string value to a typed serde_json::Value.
pub fn convert(value: &str) -> Value {
    todo!()
}
