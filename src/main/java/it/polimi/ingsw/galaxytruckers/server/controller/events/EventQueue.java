package it.polimi.ingsw.galaxytruckers.server.controller.events;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.server.controller.events.types.Event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A threadsafe queue for handling game events in the Galaxy Truckers game.
 * Implements the EventListener interface to receive events and provides methods
 * for retrieving events for processing. Uses a blocking queue to safely handle
 * events across multiple threads.
 *
 * @param <T> the type of Event that this queue will store and manage
 */
public class EventQueue<T extends Event> implements EventListener<T> {
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    /**
     * Adds an event to the queue when notified.
     * Implementation of the EventListener interface method.
     *
     * @param event the event to add to the queue
     */
    @Override
    public void notifyEvent(T event) {
        queue.add(event);
    }

    /**
     * Retrieves and removes the head of the queue, waiting if necessary
     * until an element becomes available.
     *
     * @return the head of the queue
     * @throws InterruptedException if interrupted while waiting
     */
    public T poll() throws InterruptedException {
        return queue.take();
    }

    /**
     * Clears all events from the queue.
     */
    @VisibleForTesting
    public void clear() {
        this.queue.clear();
    }
}
