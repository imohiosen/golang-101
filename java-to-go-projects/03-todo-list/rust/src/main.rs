use http_body_util::{BodyExt, Full};
use hyper::body::{self, Bytes, Incoming};
use hyper::server::conn::http1;
use hyper::service::service_fn;
use hyper::{Method, Request, Response, StatusCode};
use hyper_util::rt::TokioIo;
use serde::{Deserialize, Serialize};
use std::collections::HashMap;
use std::sync::Arc;
use tokio::net::TcpListener;
use tokio::sync::Mutex;

const HOST: &str = "127.0.0.1:8889";

#[derive(Serialize, Deserialize, Clone)]
struct Todo {
    id: u64,
    title: String,
    completed: bool,
}

type Db = Arc<Mutex<(HashMap<u64, Todo>, u64)>>;

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let db: Db = Arc::new(Mutex::new((HashMap::new(), 1)));
    let listener = TcpListener::bind(HOST).await.unwrap();
    println!("Server running on http://{}", HOST);

    loop {
        let (stream, _) = listener.accept().await.unwrap();
        let io = TokioIo::new(stream);
        let db = db.clone();

        tokio::spawn(async move {
            // Handle the connection here
            http1::Builder::new()
                .serve_connection(io, service_fn(move |req| handle(req, db.clone())))
                .await
                .unwrap();
        });
    }
}

async fn handle(req: Request<Incoming>, db: Db) -> Result<Response<Full<Bytes>>, hyper::Error> {
    let path = req.uri().path().to_string();
    let method = req.method().clone();

    match (method, path.as_str()) {
        (Method::POST, "/todos/create") => create_todo(req, db).await,
        (Method::GET, "/todos/get-all") => get_all_todos(db).await,
        (Method::GET, "/todos/get-one") => get_one_todo(req, db).await,
        (Method::PUT, "/todos/update") => update_todo(req, db).await,
        (Method::DELETE, "/todos/delete") => delete_todo(req, db).await,
        _ => not_found(),
    }
}

async fn delete_todo(
    req: Request<Incoming>,
    db: Db,
) -> Result<Response<Full<Bytes>>, hyper::Error> {
    let id: u64 =
        match get_query_params(req.uri(), "id").and_then(|id_str| id_str.parse::<u64>().ok()) {
            Some(id) => id,
            None => {
                return json_response(
                    StatusCode::BAD_REQUEST,
                    "{\"error\": \"Invalid ID\"}".to_string(),
                )
            }
        };
    let mut state = db.lock().await;

    if state.0.remove(&id).is_some() {
        json_response(
            StatusCode::OK,
            "{\"message\": \"Todo deleted\"}".to_string(),
        )
    } else {
        json_response(
            StatusCode::NOT_FOUND,
            "{\"error\": \"Todo not found\"}".to_string(),
        )
    }
}

async fn get_one_todo(
    req: Request<Incoming>,
    db: Db,
) -> Result<Response<Full<Bytes>>, hyper::Error> {
    let id = get_query_params(req.uri(), "id").and_then(|id_str| id_str.parse::<u64>().ok());
    let state = db.lock().await;
    if let Some(id) = id {
        if let Some(todo) = state.0.get(&id) {
            return json_response(StatusCode::OK, serde_json::to_string(todo).unwrap());
        }
    }
    json_response(
        StatusCode::NOT_FOUND,
        "{\"error\": \"Todo not found\"}".to_string(),
    )
}

async fn update_todo(
    req: Request<Incoming>,
    db: Db,
) -> Result<Response<Full<Bytes>>, hyper::Error> {
    let id = get_query_params(req.uri(), "id").and_then(|id_str| id_str.parse::<u64>().ok());
    let body = req.collect().await?.to_bytes();

    let mut updated_todo: Todo = match serde_json::from_slice(&body) {
        Ok(t) => t,
        Err(_) => {
            return json_response(
                StatusCode::BAD_REQUEST,
                "{\"error\": \"Invalid JSON\"}".to_string(),
            )
        }
    };
    let mut state = db.lock().await;
    if let Some(id) = id {
        if let Some(todo) = state.0.get_mut(&id) {
            *todo = updated_todo;
            return json_response(StatusCode::OK, serde_json::to_string(todo).unwrap());
        }
    }
    json_response(
        StatusCode::NOT_FOUND,
        "{\"error\": \"Todo not found\"}".to_string(),
    )
}

async fn get_all_todos(db: Db) -> Result<Response<Full<Bytes>>, hyper::Error> {
    let state = db.lock().await;
    let todos: Vec<&Todo> = state.0.values().collect();
    json_response(StatusCode::OK, serde_json::to_string(&todos).unwrap())
}

async fn create_todo(
    req: Request<Incoming>,
    db: Db,
) -> Result<Response<Full<Bytes>>, hyper::Error> {
    let body = req.collect().await?.to_bytes();
    let mut todo: Todo = match serde_json::from_slice(&body) {
        Ok(t) => t,
        Err(_) => {
            return json_response(
                StatusCode::BAD_REQUEST,
                "{\"error\": \"Invalid JSON\"}".to_string(),
            )
        }
    };

    let mut state = db.lock().await;
    todo.id = state.1;
    state.1 += 1;
    state.0.insert(todo.id, todo.clone());
    json_response(StatusCode::OK, serde_json::to_string(&todo).unwrap())
}

fn json_response(status: StatusCode, body: String) -> Result<Response<Full<Bytes>>, hyper::Error> {
    Ok(Response::builder()
        .status(status)
        .header("Content-Type", "application/json")
        .body(Full::new(Bytes::from(body)))
        .unwrap())
}

fn not_found() -> Result<Response<Full<Bytes>>, hyper::Error> {
    Ok(Response::builder()
        .status(StatusCode::NOT_FOUND)
        .body(Full::new(Bytes::from("Not Found")))
        .unwrap())
}

fn get_query_params(uri: &hyper::Uri, key: &str) -> Option<String> {
    uri.query().and_then(|q| {
        q.split('&').find_map(|pair| {
            let mut parts = pair.splitn(2, '=');
            let k = parts.next()?;
            let v = parts.next()?;
            if k == key {
                Some(v.to_string())
            } else {
                None
            }
        })
    })
}
