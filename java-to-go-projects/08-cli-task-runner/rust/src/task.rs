use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct Task {
    pub name: String,
    pub command: String,
    #[serde(default)]
    pub depends_on: Vec<String>,
    pub work_dir: Option<String>,
}
