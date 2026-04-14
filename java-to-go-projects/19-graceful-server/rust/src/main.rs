mod handler;
mod logger_middleware;
mod shutdown;
mod server;

#[tokio::main]
async fn main() {
    todo!("Parse CLI args (--port, --timeout), start server with graceful shutdown")
}
