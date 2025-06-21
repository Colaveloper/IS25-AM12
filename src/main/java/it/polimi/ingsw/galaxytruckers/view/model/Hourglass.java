package it.polimi.ingsw.galaxytruckers.view.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Hourglass {
    private final static long DURATION = 60;

    private int flipsLeft;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private final AtomicLong timeLeft = new AtomicLong(0);

    public Hourglass(int rounds) {
        this.flipsLeft = rounds;
    }

    /**
     * Flips the hourglass, if not already running, decrements the number of remaining flips
     * and executes the given task once it has finished
     * @throws IllegalStateException if the hourglass is already running
     */
    public void flip() {
        if (isRunning.get()) {
            throw new IllegalStateException("Hourglass is already running");
        }

        timeLeft.set(DURATION);
        flipsLeft--;

        start();
    }

    public void end() {
        isRunning.set(false);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    public void setup(int flipsLeft, int timeLeft, boolean isRunning) {
        end();
        this.flipsLeft = flipsLeft;
        this.timeLeft.set(timeLeft);
        if (isRunning) start();
    }

    private void start() {
        isRunning.set(true);
        scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            long currentTime = timeLeft.decrementAndGet();
            if (currentTime < 0) {
                end();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public int getFlipsLeft() {
        return flipsLeft;
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public long getTimeLeft() {
        return timeLeft.get();
    }
}
