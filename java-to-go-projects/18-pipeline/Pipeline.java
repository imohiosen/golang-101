package pipeline;

import java.util.*;

/**
 * Builder for composing multi-stage data pipelines.
 * Stages are connected by Channels and run in separate threads.
 */
public class Pipeline {

    private final List<Thread> threads = new ArrayList<>();

    /**
     * Add a stage (Runnable) to run in its own thread.
     */
    public Pipeline addStage(String name, Runnable stage) {
        Thread t = new Thread(stage, name);
        threads.add(t);
        return this;
    }

    /**
     * Start all stages concurrently.
     */
    public void start() {
        for (Thread t : threads) {
            t.start();
        }
    }

    /**
     * Wait for all stages to complete.
     */
    public void awaitCompletion() throws InterruptedException {
        for (Thread t : threads) {
            t.join();
        }
    }

    /**
     * Start the pipeline and wait for completion.
     */
    public void run() throws InterruptedException {
        start();
        awaitCompletion();
    }
}
