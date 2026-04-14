use crate::task::Task;
use std::collections::HashMap;

/// Parse a tasks.json file into a HashMap<name, Task>.
pub fn parse_file(filename: &str) -> Result<HashMap<String, Task>, String> {
    todo!()
}

/// Validate: all deps exist, no circular dependencies (DFS cycle detection).
pub fn validate(tasks: &HashMap<String, Task>) -> Result<(), String> {
    todo!()
}
