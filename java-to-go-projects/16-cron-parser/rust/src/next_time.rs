use crate::schedule::Schedule;

/// Check if a given date/time matches the schedule.
pub fn matches(
    schedule: &Schedule,
    minute: u32,
    hour: u32,
    day: u32,
    month: u32,
    weekday: u32,
) -> bool {
    todo!()
}

/// Calculate the next N execution times from a starting point.
/// Returns Vec of (year, month, day, hour, minute) tuples.
/// Iterates minute-by-minute from start.
pub fn next_n(
    schedule: &Schedule,
    start_year: i32,
    start_month: u32,
    start_day: u32,
    start_hour: u32,
    start_minute: u32,
    count: usize,
) -> Vec<(i32, u32, u32, u32, u32)> {
    todo!()
}
