use std::sync::mpsc;

/// Create a bounded channel pair. Wrapper around std::sync::mpsc::sync_channel.
pub fn bounded<T>(capacity: usize) -> (mpsc::SyncSender<T>, mpsc::Receiver<T>) {
    mpsc::sync_channel(capacity)
}
