package workerpool;

import java.util.concurrent.BlockingQueue;

/**
 * A single worker thread that pulls jobs from a shared queue and processes them.
 * Stops when it receives a poison pill (null job) or is interrupted.
 */
public class Worker<T, R> implements Runnable {

    private final int id;
    private final BlockingQueue<Job<T, R>> jobQueue;
    private final BlockingQueue<Job<T, R>> resultQueue;
    private int jobsProcessed = 0;

    public Worker(int id, BlockingQueue<Job<T, R>> jobQueue,
                  BlockingQueue<Job<T, R>> resultQueue) {
        this.id = id;
        this.jobQueue = jobQueue;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        System.out.printf("Worker-%d started%n", id);
        try {
            while (true) {
                Job<T, R> job = jobQueue.take(); // blocks until job available
                if (job.getId() == -1) {
                    // Poison pill — shutdown signal
                    break;
                }

                System.out.printf("Worker-%d processing Job#%d%n", id, job.getId());
                job.execute();
                resultQueue.put(job);
                jobsProcessed++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("Worker-%d stopped (processed %d jobs)%n", id, jobsProcessed);
    }

    public int getId() { return id; }
    public int getJobsProcessed() { return jobsProcessed; }
}
