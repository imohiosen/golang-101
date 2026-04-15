use rocksdb::IteratorMode;

/// MergingIterator merges results from multiple column families in sorted order.
/// Useful for the UNION ALL merge-on-read pattern from the architecture.
pub struct MergingIterator {
    // cursors: each holds (cf_name, current_key, current_value)
}

impl MergingIterator {
    /// Create a merging iterator over the specified column families.
    pub fn new(cf_names: Vec<String>) -> Self {
        let _ = IteratorMode::Start;
        todo!("Create an iterator per CF, initialize cursors at first position")
    }

    /// Advance to next entry (smallest key across all cursors).
    pub fn next(&mut self) -> Option<(String, Vec<u8>, Vec<u8>)> {
        todo!("Find cursor with smallest key, return (cf_name, key, value), advance that cursor")
    }

    /// Seek all cursors to the given key.
    pub fn seek(&mut self, key: &[u8]) {
        todo!("Seek each cursor to key")
    }
}
