use crate::schedule::Schedule;
use std::collections::HashSet;

/// Parse a 5-field cron expression (or named shortcut) into a Schedule.
/// Supports: *, ranges (N-M), steps (*/S, N-M/S), lists (N,M,O).
/// Named shortcuts: @yearly, @monthly, @weekly, @daily, @hourly.
pub fn parse(expression: &str) -> Result<Schedule, String> {
    todo!()
}

/// Parse a single cron field into a set of values within [min, max].
pub fn parse_field(field: &str, min: u32, max: u32) -> Result<HashSet<u32>, String> {
    todo!()
}
