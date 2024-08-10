package gsdk.source.generic;

import java.util.Queue;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Utility class for implementing main thread tasks (Queue + ConcurrentLinkedQueue).
 */
public class MainThreadTasks {
    private final Queue<Runnable> tasks;

    /**
     * Initialize main thread tasks class.
     */
    public MainThreadTasks() {
        tasks = new ConcurrentLinkedQueue<>();
    }

    /**
     * Add task to queue.
     * 
     * @param task Task.
     */
    public void addTask(Runnable task) {
        tasks.add(task);
    }

    /**
     * Is queue has any tasks.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Run tasks.
     */
    public void pollRun() {
        tasks.poll().run();
    }

    /**
     * Run tasks if any tasks available.
     */
    public void pollRunIfNotEmpty() {
        if(!isEmpty()) pollRun();
    }

    /**
     * Run tasks and do not stop while all tasks are completed.
     */
    public void runTasks() {
        while(!isEmpty()) pollRun();
    }
}
