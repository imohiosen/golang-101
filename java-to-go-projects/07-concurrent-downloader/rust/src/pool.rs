use crate::fetcher;
use crate::result::DownloadResult;

/// Download all URLs concurrently (up to max_concurrent at a time).
/// Uses tokio::spawn + Semaphore for concurrency control.
pub async fn download_all(urls: Vec<String>, output_dir: &str, max_concurrent: usize) -> Vec<DownloadResult> {
    todo!()
}
