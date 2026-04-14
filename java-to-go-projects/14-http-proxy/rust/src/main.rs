mod config;
mod rewriter;
mod proxy;
mod logger;

#[tokio::main]
async fn main() {
    todo!("Parse CLI args (--config, --port, --target), create Config, start hyper server that proxies all requests")
}
