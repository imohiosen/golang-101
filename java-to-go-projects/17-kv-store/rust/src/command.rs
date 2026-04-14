
pub enum Command {
    Get { key: String },
    Set { key: String, value: String },
    Delete { key: String },
    Keys,
    Save,
    Load,
    Quit,
    Unknown { message: String },
}

pub fn parse(input: &str) -> Command {
    let s = input.trim();

    //if empty, return unknown
    if s.is_empty() {
        return Command::Unknown {
            message: "Empty command".to_string(),
        };
    }

    let parts: Vec<&str> = s.splitn(3, ' ').collect();

    match parts[0].to_uppercase().as_str() {
        "GET" => {
            if parts.len() < 2 {
                return Command::Unknown {
                    message: "GET command requires a key".to_string(),
                };
            }
            Command::Get {
                key: parts[1].to_string(),
            }
        }
        "SET" => {
            if parts.len() < 3 {
                return Command::Unknown {
                    message: "SET command requires a key and value".to_string(),
                };
            }
            Command::Set {
                key: parts[1].to_string(),
                value: parts[2].to_string(),
            }
        }
        "DELETE"|"DEL" => {
            if parts.len() < 2 {
                return Command::Unknown {
                    message: "DELETE command requires a key".to_string(),
                };
            }
            Command::Delete {
                key: parts[1].to_string(),
            }
        }
        "KEYS" => Command::Keys,
        "SAVE" => Command::Save,
        "LOAD" => Command::Load,
        "QUIT"|"EXIT" => Command::Quit,
        _ => Command::Unknown {
            message: format!("Unknown command: {}", s),
        },
    }

}