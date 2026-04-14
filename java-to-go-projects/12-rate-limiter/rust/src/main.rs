mod token_bucket;
mod sliding_window;
mod middleware;
mod handler;

use std::sync::{Arc, Mutex};

#[tokio::main]
async fn main() {
    todo!("Parse CLI args (--algo, --limit, --window, --port), create Middleware, start hyper server on /api/data")
}
