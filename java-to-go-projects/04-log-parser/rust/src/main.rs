use regex::Regex;
use std::collections::{HashMap, HashSet};
use std::env;
use std::fs;

struct LogEntry {
    timestamp: String,
    level: String,
    path: String,
    message: String,
}

fn parse_file(filename: &str) -> Vec<LogEntry> {
    let content = fs::read_to_string(filename).expect("Failed to read log file");
    let re = Regex::new(r"\[(.*?)\] (\w+)\s+(\S+) - (.*)").unwrap();

    content
        .lines()
        .filter_map(|line| {
            let caps = re.captures(line)?;
            Some(LogEntry {
                timestamp: caps[1].to_string(),
                level: caps[2].to_string(),
                path: caps[3].to_string(),
                message: caps[4].to_string(),
            })
        })
        .collect()

}



fn count_by_level(entries: &[LogEntry]) -> HashMap<String, usize> {
    let mut counts = HashMap::new();
    for entry in entries {
        *counts.entry(entry.level.clone()).or_insert(0) += 1;
    }
    counts
}

fn filter_by_level<'a>(entries: &'a [LogEntry], level: &str) -> Vec<&'a LogEntry> {
    entries.iter()
        .filter(|e| e.level.eq_ignore_ascii_case(level))
        .collect()
}

fn unique_paths(entries: &[LogEntry]) -> HashSet<String> {
    entries.iter().map(|e| e.path.clone()).collect()
}

fn main() {
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        eprintln!("Usage: {} <logfile>", args[0]);
        return;
    }

    let entries = parse_file(&args[1]);
    println!("Total log entries: {}", entries.len());

    let counts = count_by_level(&entries);
    for (level, count) in counts {
        println!("{}: {}", level, count);
    }

    let error_entries = filter_by_level(&entries, "ERROR");
    println!("Error entries:");
    for entry in error_entries {
        println!("[{}] {} - {}", entry.timestamp, entry.path, entry.message);
    }

    let paths = unique_paths(&entries);
    println!("Unique paths:");
    for path in paths {
        println!("{}", path);
    }
}