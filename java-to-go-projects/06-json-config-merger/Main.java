package merger;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * CLI entry point for the JSON config merger.
 *
 * Usage: java Main base.json override1.json [override2.json ...]
 *
 * Merges multiple config files left-to-right. Later files override earlier ones.
 *
 * Go structure:
 *   main.go              — CLI parsing, orchestration
 *   merger/merge.go      — Merge(base, override) using map[string]interface{}
 *   merger/loader.go     — LoadFile, PrettyPrint
 *   merger/merge_test.go — Table-driven tests for merge edge cases
 *
 * Rust structure:
 *   main.rs              — CLI parsing
 *   merger.rs            — merge(Value, Value) -> Value using serde_json::Value
 *   loader.rs            — load_file, pretty_print
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: config-merger <base.json> <override.json> [more...]");
            System.exit(1);
        }

        JsonNode result = Loader.loadFile(args[0]);
        System.out.println("Base: " + args[0]);

        for (int i = 1; i < args.length; i++) {
            System.out.println("Merging: " + args[i]);
            JsonNode override = Loader.loadFile(args[i]);
            result = Merger.merge(result, override);
        }

        System.out.println("\nResult:");
        System.out.println(Loader.prettyPrint(result));
    }
}
