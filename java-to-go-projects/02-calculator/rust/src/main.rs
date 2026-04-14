use http_body_util::Full;
use hyper::body::Bytes;
use hyper::server::conn::http1;
use hyper::service::service_fn;
use hyper::{Method, Request, Response, StatusCode};
use hyper_util::rt::TokioIo;
use tokio::net::TcpListener;

// Parse a query param by name from a query string like "a=10&b=3"
fn get_param(query: &str, key: &str) -> Option<f64> {
    query
        .split('&')
        .find(|pair| pair.starts_with(&format!("{}=", key)))
        .and_then(|pair| pair.split('=').nth(1))
        .and_then(|val| val.parse::<f64>().ok())
}

// Build a plain text response
fn text_response(status: StatusCode, body: String) -> Response<Full<Bytes>> {
    Response::builder()
        .status(status)
        .body(Full::new(Bytes::from(body)))
        .unwrap()
}

// Main request handler — routes by path
async fn handle(req: Request<hyper::body::Incoming>) -> Result<Response<Full<Bytes>>, hyper::Error> {
    if req.method() != Method::GET {
        return Ok(text_response(
            StatusCode::METHOD_NOT_ALLOWED,
            "Method not allowed".to_string(),
        ));
    }

    let query = req.uri().query().unwrap_or("");
    let a = get_param(query, "a");
    let b = get_param(query, "b");

    // Validate both params are present and valid
    let (a, b) = match (a, b) {
        (Some(a), Some(b)) => (a, b),
        (None, _) => return Ok(text_response(StatusCode::BAD_REQUEST, "Invalid parameter 'a'".to_string())),
        (_, None) => return Ok(text_response(StatusCode::BAD_REQUEST, "Invalid parameter 'b'".to_string())),
    };

    let path = req.uri().path();

    let result = match path {
        "/add"      => format!("Result: {:.2}", a + b),
        "/subtract" => format!("Result: {:.2}", a - b),
        "/multiply" => format!("Result: {:.2}", a * b),
        "/divide"   => {
            if b == 0.0 {
                return Ok(text_response(StatusCode::BAD_REQUEST, "Division by zero is not allowed".to_string()));
            }
            format!("Result: {:.2}", a / b)
        }
        _ => return Ok(text_response(StatusCode::NOT_FOUND, "Not found".to_string())),
    };

    Ok(text_response(StatusCode::OK, result))
}

#[tokio::main]
async fn main() {
    let addr = "0.0.0.0:8082";
    let listener = TcpListener::bind(addr).await.unwrap();
    println!("Server running on http://{}", addr);

    loop {
        let (stream, _) = listener.accept().await.unwrap();
        let io = TokioIo::new(stream);

        // Spawn a new task for each connection
        tokio::spawn(async move {
            http1::Builder::new()
                .serve_connection(io, service_fn(handle))
                .await
                .unwrap();
        });
    }
}
