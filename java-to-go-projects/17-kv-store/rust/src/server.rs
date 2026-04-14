
use tokio::net::TcpListener;
use tokio::io::{AsyncBufReadExt, AsyncWriteExt, BufReader};
use std::sync::Arc;
use tokio::sync::RwLock;
use crate::store::Store;
use crate::command::{self, Command};
use crate::persistence;


pub async fn run(addr: &str, store: Arc<RwLock<Store>>, data_file: String) -> Result<(), String> {
    let tcp_listener = TcpListener::bind(addr)
    .await.map_err(|e| format!("Failed to bind to address {}: {}", addr, e))?;
    println!("Server running on {}", addr);
    loop {
        let (socket, client_addr) = tcp_listener.accept()
            .await.map_err(|e| format!("Failed to accept connection: {}", e))
            .map(|(socket, client_addr)| {
                println!("New client connected: {}", client_addr);
                (socket, client_addr)
            })?;

        let store: Arc<RwLock<Store>> = Arc::clone(&store);
        let data_file = data_file.clone();
        tokio::spawn(async move {
            handle_client(socket, store, data_file).await;
        });

    }    

}

async fn handle_client(socket: tokio::net::TcpStream, store: Arc<RwLock<Store>>, data_file: String) {
    let (reader, mut writer) = socket.into_split();
    let mut reader = BufReader::new(reader).lines();

    let _ = writer.write_all(b"Welcome to the KV Store Server!\n Type QUIT to disconnect.\n").await;

    while let Ok(Some(line)) = reader.next_line().await {
        let cmd = command::parse(&line);
        let is_quit = matches!(cmd, Command::Quit);
        let response = execute(cmd, &store, &data_file).await;
        let _ = writer.write_all(response.as_bytes()).await;
        if is_quit {
            break;
        }
    }

}

async fn execute(cmd: Command, store: &Arc<RwLock<Store>>, data_file: &str) -> String {
    match cmd {
        Command::Get { key } => {
            let store = store.read().await;
            match store.get(&key) {
                Some(value) => format!("OK: {}\n", value),
                None => "Key not found\n".to_string(),
            }
        }
        Command::Set { key, value } => {
            let mut store = store.write().await;
            store.set(key, value);

            "OK\n".to_string()
        }
        Command::Quit => {
            "Goodbye!\n".to_string()
        } 
        Command::Delete { key } => {
            let mut store = store.write().await;
            let delete = store.delete(&key);
            if delete {
                "Deleted\n".to_string()
            } else {
                "Key not found\n".to_string()
            }
        }
        Command::Keys => {
            let store = store.read().await;
            let keys = store.keys();
            if keys.is_empty() {
                "No keys found\n".to_string()
            } else {
                format!("Keys: {}\n", keys.iter().map(|k| k.as_str()).collect::<Vec<_>>().join(", "))  
            }
        }
        Command::Save => {
            let store = store.read().await;
            if let Err(e) = persistence::save(&store.snapshot(), data_file) {
                return format!("Error saving data: {}\n", e);
            }
            "OK\n".to_string()
        }
        Command::Load => {
            match persistence::load(data_file) {
                Ok(data) => {
                    let mut store = store.write().await;
                    store.replace_all(data);
                    "OK\n".to_string()
                }
                Err(e) => format!("Error loading data: {}\n", e),
            }
        }
        Command::Unknown { message } => format!("Error: {}\n", message),
    }
}