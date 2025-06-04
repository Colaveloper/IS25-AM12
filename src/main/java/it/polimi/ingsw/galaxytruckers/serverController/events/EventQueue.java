package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventQueue<T extends Event> implements EventListener<T> {
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    @Override
    public void notifyEvent(T event) {
        if (!queue.offer(event)) {
            throw new RuntimeException("Queue is full");
        }
    }

    public T poll() throws InterruptedException {
        return queue.take();
    }
}
