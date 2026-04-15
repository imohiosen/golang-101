mod schema;
mod serde_avro;

fn main() {
    println!("Avro Serde + Schema Evolution Demo");
    todo!("1) Create v1 codec, encode event, 2) evolve to v2, encode with new field, 3) read v1 data with v2 schema (defaults applied), 4) demonstrate v3 union type, 5) show incompatible change rejection")
}
