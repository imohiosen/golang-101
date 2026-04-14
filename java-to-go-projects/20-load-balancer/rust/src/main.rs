mod config;
mod strategy;
mod health;
mod forwarder;
mod balancer;

#[tokio::main]
async fn main() {
    todo!("Parse CLI args (--port, --strategy, --backend=url repeatable), build Config, create BackendStates, spawn health checker, start hyper server")
}
