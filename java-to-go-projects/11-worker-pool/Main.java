package workerpool;

import java.util.*;

/**
 * Demo: Worker pool processing simulated tasks.
 *
 * Usage: java Main [numJobs] [numWorkers]
 *
 * Go structure:
 *   main.go                   — Create pool, submit jobs, collect results
 *   workerpool/job.go         — Job struct with Execute(), uses interface{} or generics
 *   workerpool/worker.go      — Worker goroutine: reads from jobs chan, writes to results chan
 *   workerpool/pool.go        — Pool: Start (launch goroutines), Submit, Shutdown, CollectResults
 *                                Uses channels: jobs chan Job, results chan Job
 *
 * Rust structure:
 *   main.rs                   — Create pool, submit jobs, collect results
 *   job.rs                    — Job<T,R> with execute(), Box<dyn Fn(T)->R + Send>
 *   worker.rs                 — Worker: loop recv from crossbeam/mpsc channel
 *   pool.rs                   — Pool: spawn threads, submit via Sender, shutdown with drop
 *
 * Key learning:
 *   - Go: Channels as job/result queues, goroutines as workers, WaitGroup/close for shutdown
 *   - Rust: mpsc channels or crossbeam, Arc<Mutex<Receiver>>, thread::spawn, Send+Sync bounds
 */
public class Main {

    public static void main(String[] args) throws Exception {
        int numJobs = 20;
        int numWorkers = 4;

        if (args.length >= 1) numJobs = Integer.parseInt(args[0]);
        if (args.length >= 2) numWorkers = Integer.parseInt(args[1]);

        System.out.printf("Starting pool: %d workers, %d jobs%n%n", numWorkers, numJobs);

        Pool<Integer, String> pool = new Pool<>(numWorkers);
        pool.start();

        // Submit jobs — each simulates work with a sleep
        for (int i = 0; i < numJobs; i++) {
            final int jobNum = i;
            pool.submit(new Job<>(i, jobNum, input -> {
                try {
                    Thread.sleep((long) (Math.random() * 500 + 100));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "result_" + (input * input);
            }));
        }

        // Collect all results
        var results = pool.collectResults(numJobs);
        pool.shutdown();

        System.out.println("\n=== Results ===");
        long successes = results.stream().filter(Job::isSuccess).count();
        long failures = results.size() - successes;
        System.out.printf("Total: %d  |  OK: %d  |  Failed: %d%n", numJobs, successes, failures);
        results.forEach(r -> System.out.println("  " + r));
    }
}
