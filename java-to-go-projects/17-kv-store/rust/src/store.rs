use std::collections::HashMap;

pub struct Store {
    data: HashMap<String, String>,
}

impl Store {
    pub fn new() -> Self{
        Store {
            data: HashMap::new(),
        }
    }
    pub fn get(&self, key: &str) -> Option<&String>{
        self.data.get(key)
    }
    pub fn set(&mut self, key: String, value: String){
        self.data.insert(key, value);
    }
    pub fn delete(&mut self, key: &str) -> bool{
        self.data.remove(key).is_some()
    }
    pub fn keys(&self) -> Vec<&String>{
        self.data.keys().collect()
    }
    pub fn size(&self) -> usize{
        self.data.len()
    }
    pub fn snapshot(&self) -> HashMap<String, String>{
        self.data.clone()
    }
    pub fn replace_all(&mut self, data: HashMap<String, String>){
        self.data = data;
    }
}