use std::collections::HashMap;
use std::fs;
use std::path::Path;

pub fn save(data: &HashMap<String, String>, file_path: &str) -> Result<(), String> {
    serde_json::to_string_pretty(data)
        .map_err(|e| e.to_string())
        .and_then(|json| {
            fs::write(file_path, json).map_err(|e| format!("Failed to save data: {}", e))
        })
        .map(|_| println!("Data successfully saved to {}", file_path))
}

pub fn load(file_path: &str) -> Result<HashMap<String, String>, String> {
    let path = Path::new(file_path);
    if !path.exists() {
        return Ok(HashMap::new());
    }
    let data = fs::read_to_string(file_path).map_err(|e| format!("Failed to read data: {}", e))?;
    serde_json::from_str(&data)
        .map_err(|e| format!("Failed to parse data: {}", e))
        .map(|map: HashMap<String, String>| {
            println!("Data successfully loaded from {}", file_path);
            map
        })
}
