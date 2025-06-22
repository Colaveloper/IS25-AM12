package it.polimi.ingsw.galaxytruckers.serverController.events;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventQueue<T extends Event> implements EventListener<T> {
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    @Override
    public void notifyEvent(T event) {
        queue.add(event);
    }

    public T poll() throws InterruptedException {
        return queue.take();
    }

    @VisibleForTesting
    public void clear() {
        this.queue.clear();
    }
}
