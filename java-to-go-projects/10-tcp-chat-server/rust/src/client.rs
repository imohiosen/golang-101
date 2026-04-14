use crate::room::Room;
use crate::message::Message;
use std::collections::HashMap;
use std::sync::Arc;
use tokio::sync::RwLock;
use tokio::net::TcpStream;

/// Handle a single connected client: read commands, dispatch to rooms.
/// Commands: /nick, /join, /leave, /rooms, /who, /quit, or plain text.
pub async fn handle_client(
    socket: TcpStream,
    rooms: Arc<RwLock<HashMap<String, Arc<Room>>>>,
) {
    todo!()
}
