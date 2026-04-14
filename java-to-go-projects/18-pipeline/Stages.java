package pipeline;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Collection of reusable pipeline stage functions.
 * Each stage reads from an input Channel and writes to an output Channel.
 *
 * Stages run in their own thread, processing items as they arrive.
 */
public class Stages {

    /**
     * Map stage: transforms each input item using the given function.
     */
    public static <T, R> Runnable map(Channel<T> in, Channel<R> out, Function<T, R> fn) {
        return () -> {
            try {
                while (true) {
                    T item = in.receive();
                    if (item == null && in.isClosed()) break;
                    if (item == null) continue;
                    out.send(fn.apply(item));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                out.close();
            }
        };
    }

    /**
     * Filter stage: only passes items that match the predicate.
     */
    public static <T> Runnable filter(Channel<T> in, Channel<T> out, Predicate<T> pred) {
        return () -> {
            try {
                while (true) {
                    T item = in.receive();
                    if (item == null && in.isClosed()) break;
                    if (item == null) continue;
                    if (pred.test(item)) {
                        out.send(item);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                out.close();
            }
        };
    }

    /**
     * Sink stage: collects all items into a list (terminal stage).
     */
    public static <T> Runnable sink(Channel<T> in, java.util.List<T> results) {
        return () -> {
            try {
                while (true) {
                    T item = in.receive();
                    if (item == null && in.isClosed()) break;
                    if (item == null) continue;
                    synchronized (results) {
                        results.add(item);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }

    /**
     * Fan-out: duplicate input to multiple output channels.
     */
    public static <T> Runnable fanOut(Channel<T> in, java.util.List<Channel<T>> outs) {
        return () -> {
            try {
                while (true) {
                    T item = in.receive();
                    if (item == null && in.isClosed()) break;
                    if (item == null) continue;
                    for (Channel<T> out : outs) {
                        out.send(item);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                for (Channel<T> out : outs) out.close();
            }
        };
    }
}
