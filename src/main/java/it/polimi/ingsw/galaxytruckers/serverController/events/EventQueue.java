package it.polimi.ingsw.galaxytruckers.serverController.events;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventQueue implements EventListener {
    private final BlockingQueue<Event> queue = new LinkedBlockingQueue<>();

    @Override
    public void notifyEvent(Event event) {
        if (!enqueue(event)) {
            throw new IllegalStateException("Queue is full");
        }
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized boolean enqueue(Event event) {
        return this.queue.offer(event);
    }

    public synchronized Event dequeue() throws InterruptedException {
        return this.queue.take();
    }
}
