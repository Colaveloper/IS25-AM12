package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

/**
 * Abstract base class for event queue handlers in the Galaxy Truckers game.
 * Handles the threading and event polling logic for processing events from an EventQueue.
 * Implementing classes need only to define the specific event handling behavior.
 *
 * @param <T> the specific type of Event that this handler can process
 */
public abstract class EventQueueHandler<T extends Event> implements EventHandler<T> {
    private final EventQueue<T> eventQueue;
    private Thread thread;
    private Runnable afterEach = () -> {};
    private boolean isRunning = false;

    /**
     * Creates a new EventQueueHandler with the specified event queue.
     *
     * @param queue the event queue to poll for events
     */
    public EventQueueHandler(EventQueue<T> queue) {
        this.eventQueue = queue;
    }

    /**
     * Sets a runnable to be executed after each event is handled.
     * This can be used for cleanup or additional processing.
     *
     * @param afterEach the runnable to execute after each event
     */
    public void setAfterEach(Runnable afterEach) {
        this.afterEach = afterEach;
    }

    /**
     * Starts the event handling thread.
     * If the thread is not already created, creates a new thread.
     * Sets the thread name to the simple class name of the implementing class.
     */
    public void start() {
        if (this.thread == null) {
            this.thread = new Thread(this::threadTask, this.getClass().getSimpleName()+"-thread");
        }
        isRunning = true;
        this.thread.start();
    }

    /**
     * Stops the event handling thread.
     * The thread will terminate after handling any current event.
     */
    public void stop() {
        this.isRunning = false;
    }

    /**
     * Internal thread task that continuously polls the event queue for events
     * and passes them to the handleEvent method for processing.
     * Also executes the afterEach runnable after each event is handled.
     * Runs until stop() is called or the thread is interrupted.
     */
    private void threadTask() {
        try {
            while (isRunning) {
                T event = eventQueue.poll();
                handleEvent(event);
                afterEach.run();
            }
        } catch (InterruptedException e) {
            System.out.println(this.getClass() + ": thread interrupted");
            Thread.currentThread().interrupt();
        }
    }
}
