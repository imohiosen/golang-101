package workerpool;

import java.util.*;
import java.util.concurrent.*;

/**
 * Manages a fixed-size pool of worker threads.
 * Jobs are submitted to a shared queue; workers pull and process them.
 * Results are collected from a result queue.
 */
public class Pool<T, R> {

    private final int size;
    private final BlockingQueue<Job<T, R>> jobQueue;
    private final BlockingQueue<Job<T, R>> resultQueue;
    private final List<Thread> threads = new ArrayList<>();
    private final List<Worker<T, R>> workers = new ArrayList<>();

    public Pool(int size) {
        this.size = size;
        this.jobQueue = new LinkedBlockingQueue<>();
        this.resultQueue = new LinkedBlockingQueue<>();
    }

    /**
     * Start all worker threads.
     */
    public void start() {
        for (int i = 0; i < size; i++) {
            Worker<T, R> worker = new Worker<>(i, jobQueue, resultQueue);
            workers.add(worker);
            Thread t = new Thread(worker, "Worker-" + i);
            threads.add(t);
            t.start();
        }
    }

    /**
     * Submit a job to the pool.
     */
    public void submit(Job<T, R> job) {
        jobQueue.add(job);
    }

    /**
     * Signal all workers to stop, then wait for them to finish.
     */
    @SuppressWarnings("unchecked")
    public void shutdown() throws InterruptedException {
        // Send poison pills
        for (int i = 0; i < size; i++) {
            jobQueue.put((Job<T, R>) new Job<>(-1, null, x -> null));
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    /**
     * Collect all completed results (blocking until expectedCount results available).
     */
    public List<Job<T, R>> collectResults(int expectedCount) throws InterruptedException {
        List<Job<T, R>> results = new ArrayList<>();
        for (int i = 0; i < expectedCount; i++) {
            results.add(resultQueue.take());
        }
        return results;
    }
}
