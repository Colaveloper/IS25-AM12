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
    private long duration = DURATION;
    private final AtomicLong timeLeft = new AtomicLong(0);
    private Runnable onEndCallback;

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
        if (isRunning.get()) {
            throw new IllegalStateException("Hourglass is already running");
        }

        timeLeft.set(duration);
        isRunning.set(true);
        flipsLeft--;

        scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            long currentTime = timeLeft.decrementAndGet();
            if (currentTime < 0) {
                end();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void end() {
        if (isRunning.get()) {
            isRunning.set(false);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
            if (onEndCallback != null) {
                onEndCallback.run();
            }
        }
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

    public long getTimeLeft() {
        return timeLeft.get();
    }
}
