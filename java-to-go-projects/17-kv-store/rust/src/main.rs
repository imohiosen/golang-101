

mod command;
mod server;
mod persistence;
mod store;

use std::sync::Arc;
use tokio::sync::RwLock;

#[tokio::main]
async fn main() {
    let port = 6379;
    let data_file = "data.json".to_string();
    let store = Arc::new(RwLock::new(store::Store::new()));

    // Auto-load if data file exists
    if std::path::Path::new(&data_file).exists() {
        match persistence::load(&data_file) {
            Ok(data) => {
                store.write().await.replace_all(data);
            }
            Err(e) => eprintln!("Warning: {}", e),
        }
    }

    let addr = format!("127.0.0.1:{}", port);
    if let Err(e) = server::run(&addr, store, data_file).await {
        eprintln!("Server error: {}", e);
    }
}