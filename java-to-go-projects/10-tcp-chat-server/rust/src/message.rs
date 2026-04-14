use std::fmt;

pub struct Message {
    pub sender: String,
    pub room: String,
    pub content: String,
}

impl Message {
    pub fn new(sender: &str, room: &str, content: &str) -> Self {
        Message {
            sender: sender.to_string(),
            room: room.to_string(),
            content: content.to_string(),
        }
    }
}

impl fmt::Display for Message {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "[{}] {}: {}", self.room, self.sender, self.content)
    }
}
