package it.polimi.ingsw.galaxytruckers.view.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Hourglass {
    private final static long DURATION = 60;

    private int flipsLeft;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private long duration = DURATION;

    public Hourglass(int rounds) {
        this.flipsLeft = rounds;
    }

    public Hourglass() {
        this.flipsLeft = -1;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * @return {@code true} if the next hourglass flip is the last one,
     * {@code false} otherwise
     */
    public boolean isLastFlip() {
        return this.flipsLeft == 1;
    }

    /**
     * Flips the hourglass, if not already running, decrements the number of remaining flips
     * and executes the given task once it has finished
     * @throws IllegalStateException if the hourglass is already running
     */
    public void flip() {
        long timeLeft = duration;
        isRunning.set(true);
        flipsLeft--;
        ScheduledFuture<?> res = scheduler.scheduleAtFixedRate(() -> {
            if (timeLeft > 0) {
                if (timeLeft%10 == 0) System.out.println("Time left: " + timeLeft + " seconds");
            } else {
                end();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void end() {
        isRunning.set(false);
        scheduledFuture.cancel(true);
    }

    public int getFlipsLeft() {
        return flipsLeft;
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public long getDuration() {
        return duration;
    }
}
