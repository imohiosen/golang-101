use std::fmt;

pub struct DownloadResult {
    pub url: String,
    pub status_code: u16,
    pub bytes_downloaded: u64,
    pub duration_ms: u64,
    pub error: Option<String>,
    pub saved_path: Option<String>,
}

impl DownloadResult {
    pub fn is_success(&self) -> bool {
        self.error.is_none() && self.status_code == 200
    }
}

impl fmt::Display for DownloadResult {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        todo!()
    }
}
