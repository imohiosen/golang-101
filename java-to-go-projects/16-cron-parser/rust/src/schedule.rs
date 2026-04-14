use std::collections::HashSet;
use std::fmt;

pub struct Schedule {
    pub minutes: HashSet<u32>,       // 0-59
    pub hours: HashSet<u32>,         // 0-23
    pub days_of_month: HashSet<u32>, // 1-31
    pub months: HashSet<u32>,        // 1-12
    pub days_of_week: HashSet<u32>,  // 0-6 (Sun=0)
    pub original: String,
}

impl fmt::Display for Schedule {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        todo!()
    }
}
