package pipeline;

import java.util.*;

/**
 * Demo: Multi-stage data processing pipeline.
 *
 * Pipeline:  source → filter (even) → map (square) → sink (collect)
 *
 * Go structure:
 *   main.go                 — Build and run pipeline, source goroutine
 *   pipeline/channel.go     — Thin wrapper around chan (or just use chan directly)
 *   pipeline/stages.go      — Map, Filter, Sink, FanOut — each takes in/out chans
 *                              Run as goroutines, close output chan when done
 *   pipeline/pipeline.go    — Pipeline builder: AddStage, Run (WaitGroup for sync)
 *
 *   Key Go pattern: for item := range inputChan { ... }
 *   Close channel to signal completion to downstream
 *
 * Rust structure:
 *   main.rs                 — Build and run pipeline
 *   channel.rs              — Wrapper around crossbeam::channel or std::sync::mpsc
 *   stages.rs               — map_stage, filter_stage, sink_stage — each spawns a thread
 *                              Returns JoinHandle for awaiting
 *   pipeline.rs             — Pipeline struct, add_stage, run (join all handles)
 *
 *   Key Rust pattern: while let Ok(item) = rx.recv() { ... }
 *   Drop sender to signal completion
 *
 * Key learning:
 *   - Go: Goroutines + channels as first-class pipeline primitives, range over chan, close()
 *   - Rust: mpsc::channel, thread::spawn, move closures, Iterator adapters vs manual channels
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Pipeline Demo ===\n");

        // Create channels connecting stages
        Channel<Integer> source = new Channel<>(50);
        Channel<Integer> filtered = new Channel<>(50);
        Channel<Integer> mapped = new Channel<>(50);
        List<Integer> results = Collections.synchronizedList(new ArrayList<>());

        Pipeline pipeline = new Pipeline()
                .addStage("filter", Stages.filter(source, filtered, n -> n % 2 == 0))
                .addStage("map", Stages.map(filtered, mapped, n -> n * n))
                .addStage("sink", Stages.sink(mapped, results));

        pipeline.start();

        // Source: feed numbers into the pipeline
        System.out.println("Feeding numbers 1-20 into pipeline...");
        for (int i = 1; i <= 20; i++) {
            source.send(i);
        }
        source.close();

        pipeline.awaitCompletion();

        System.out.println("\nPipeline: source → filter(even) → map(square) → sink");
        System.out.println("Input:  1..20");
        System.out.println("Output: " + results);
        System.out.println("Count:  " + results.size());
    }
}
