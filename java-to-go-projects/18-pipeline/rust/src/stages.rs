use std::sync::mpsc::{Receiver, SyncSender};
use std::sync::{Arc, Mutex};
use std::thread::JoinHandle;

/// Spawn a thread that reads from rx, applies f to each item, sends result to tx.
/// Closes tx (drops) when rx is exhausted.
pub fn map_stage<T, U>(
    rx: Receiver<T>,
    tx: SyncSender<U>,
    f: impl Fn(T) -> U + Send + 'static,
) -> JoinHandle<()>
where
    T: Send + 'static,
    U: Send + 'static,
{
    todo!()
}

/// Spawn a thread that reads from rx, keeps items where predicate is true, sends to tx.
pub fn filter_stage<T>(
    rx: Receiver<T>,
    tx: SyncSender<T>,
    predicate: impl Fn(&T) -> bool + Send + 'static,
) -> JoinHandle<()>
where
    T: Send + 'static,
{
    todo!()
}

/// Spawn a thread that reads from rx and collects all items into results vec.
pub fn sink_stage<T>(
    rx: Receiver<T>,
    results: Arc<Mutex<Vec<T>>>,
) -> JoinHandle<()>
where
    T: Send + 'static,
{
    todo!()
}

/// Spawn a thread that reads from rx and sends each item to all output senders (fan-out).
pub fn fan_out<T>(
    rx: Receiver<T>,
    txs: Vec<SyncSender<T>>,
) -> JoinHandle<()>
where
    T: Send + Clone + 'static,
{
    todo!()
}
