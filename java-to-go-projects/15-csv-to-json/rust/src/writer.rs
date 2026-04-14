use crate::reader::CsvData;

/// Convert CsvData to JSON array of objects (using headers as keys).
/// If detect_types is true, convert values to typed JSON values.
pub fn to_json_objects(data: &CsvData, detect_types: bool) -> String {
    todo!()
}

/// Convert CsvData to JSON array of arrays (no header keys).
pub fn to_json_arrays(data: &CsvData) -> String {
    todo!()
}

/// Write JSON string to a file.
pub fn write_to_file(json: &str, filename: &str) -> Result<(), String> {
    todo!()
}
