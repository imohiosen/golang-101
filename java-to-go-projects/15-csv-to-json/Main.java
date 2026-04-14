package csv2json;

/**
 * CLI entry point for the CSV to JSON converter.
 *
 * Usage: java Main input.csv [output.json] [--no-types] [--arrays] [--delimiter=;]
 *
 * Go structure:
 *   main.go                     — CLI arg parsing, orchestration
 *   csv2json/reader.go          — CsvReader: Read(filename), parseLine with quote handling
 *   csv2json/type_detector.go   — Detect(value) → DataType, Convert(value) → interface{}
 *   csv2json/writer.go          — ToJsonObjects, ToJsonArrays using encoding/json
 *
 * Rust structure:
 *   main.rs                     — CLI parsing with std::env
 *   reader.rs                   — CsvReader: read(filename), parse_line
 *   type_detector.rs            — detect(value) → DataType enum, convert(value) → serde_json::Value
 *   writer.rs                   — to_json_objects, to_json_arrays using serde_json
 *
 * Key learning:
 *   - Go: bufio.Scanner, strings.Builder, encoding/json, interface{} for dynamic typing
 *   - Rust: String parsing, enum for DataType, serde_json::Value::Number/Bool/Null
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: csv2json <input.csv> [output.json] [--no-types] [--arrays] [--delimiter=;]");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = null;
        boolean detectTypes = true;
        boolean arrayFormat = false;
        char delimiter = ',';

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--no-types")) {
                detectTypes = false;
            } else if (args[i].equals("--arrays")) {
                arrayFormat = true;
            } else if (args[i].startsWith("--delimiter=")) {
                delimiter = args[i].split("=")[1].charAt(0);
            } else {
                outputFile = args[i];
            }
        }

        CsvReader reader = new CsvReader(delimiter, true);
        CsvReader.CsvData data = reader.read(inputFile);

        System.out.printf("Read %d rows, %d columns%n", data.rows().size(), data.headers().size());

        String json;
        if (arrayFormat) {
            json = JsonWriter.toJsonArrays(data);
        } else {
            json = JsonWriter.toJsonObjects(data, detectTypes);
        }

        if (outputFile != null) {
            JsonWriter.writeToFile(json, outputFile);
            System.out.println("Written to " + outputFile);
        } else {
            System.out.println(json);
        }
    }
}
