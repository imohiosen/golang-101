use crate::task::Task;
use std::collections::{HashMap, HashSet};

/// Run a named task and all its dependencies (depth-first).
/// Uses std::process::Command for shell execution.
pub fn run(task_name: &str, tasks: &HashMap<String, Task>, executed: &mut HashSet<String>) -> Result<(), String> {
    todo!()
}

/// Run all tasks in dependency order (topological sort).
pub fn run_all(tasks: &HashMap<String, Task>) -> Result<(), String> {
    todo!()
}

/// Topological sort of tasks by dependencies.
pub fn topological_sort(tasks: &HashMap<String, Task>) -> Vec<String> {
    todo!()
}
