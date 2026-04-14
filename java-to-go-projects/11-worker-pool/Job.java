package workerpool;

import java.util.function.Function;

/**
 * Represents a unit of work to be processed by a worker.
 * Contains input data, a processing function, and an ID for tracking.
 */
public class Job<T, R> {
    private final int id;
    private final T input;
    private final Function<T, R> processor;
    private R result;
    private Exception error;
    private long processingTimeMs;

    public Job(int id, T input, Function<T, R> processor) {
        this.id = id;
        this.input = input;
        this.processor = processor;
    }

    /**
     * Execute the job's processor function. Captures result or error.
     */
    public void execute() {
        long start = System.currentTimeMillis();
        try {
            this.result = processor.apply(input);
        } catch (Exception e) {
            this.error = e;
        }
        this.processingTimeMs = System.currentTimeMillis() - start;
    }

    public int getId() { return id; }
    public T getInput() { return input; }
    public R getResult() { return result; }
    public Exception getError() { return error; }
    public boolean isSuccess() { return error == null; }
    public long getProcessingTimeMs() { return processingTimeMs; }

    @Override
    public String toString() {
        if (isSuccess()) {
            return String.format("Job#%d: %s → %s (%dms)", id, input, result, processingTimeMs);
        }
        return String.format("Job#%d: %s → ERROR: %s", id, input, error.getMessage());
    }
}
