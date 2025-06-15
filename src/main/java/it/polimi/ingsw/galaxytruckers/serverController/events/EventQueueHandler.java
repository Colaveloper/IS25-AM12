package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

public abstract class EventQueueHandler<T extends Event> implements EventHandler<T> {
    private final EventQueue<T> eventQueue;
    private Thread thread;
    private Runnable afterEach = () -> {};
    private boolean isRunning = false;

    public EventQueueHandler(EventQueue<T> queue) {
        this.eventQueue = queue;
    }

    public void setAfterEach(Runnable afterEach) {
        this.afterEach = afterEach;
    }

    public void start() {
        if (this.thread == null) {
            this.thread = new Thread(this::threadTask, this.getClass().getSimpleName()+"-thread");
        }
        isRunning = true;
        this.thread.start();
    }

    public void stop() {
        this.isRunning = false;
    }

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
