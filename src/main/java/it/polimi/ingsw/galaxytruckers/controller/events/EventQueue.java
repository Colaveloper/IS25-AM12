package it.polimi.ingsw.galaxytruckers.controller.events;

import java.util.ArrayDeque;
import java.util.Queue;

public class EventQueue {
    private static EventQueue instance;

    private final Queue<Event> queue;

    public synchronized static EventQueue getInstance() {
        if (instance == null) {
            instance = new EventQueue();
        }
        return instance;
    }

    private EventQueue() {
        this.queue = new ArrayDeque<>();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized void enqueue(Event event) {
        this.queue.add(event);
    }

    public synchronized Event dequeue() {
        return this.queue.poll();
    }
}
