use crate::message::Message;
use tokio::sync::broadcast;

pub struct Room {
    pub name: String,
    pub tx: broadcast::Sender<String>,
}

impl Room {
    pub fn new(name: &str) -> Self {
        let (tx, _) = broadcast::channel(100);
        Room {
            name: name.to_string(),
            tx,
        }
    }

    /// Broadcast a message to all clients in this room.
    pub fn broadcast(&self, msg: &Message) {
        todo!()
    }

    /// Get a new receiver for this room's broadcast channel.
    pub fn subscribe(&self) -> broadcast::Receiver<String> {
        self.tx.subscribe()
    }
}
