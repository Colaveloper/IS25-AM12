package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Hourglass {
    private final static long DURATION = 60000;

    private final AtomicInteger flipsLeft = new AtomicInteger();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private long duration = DURATION;

    public Hourglass(int rounds) {
        this.flipsLeft.set(rounds);
    }

    @VisibleForTesting
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @VisibleForTesting
    public void stop() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            isRunning.set(false);
        }
    }

    /**
     * @return {@code true} if the next hourglass flip is the last one,
     * {@code false} otherwise
     */
    public boolean isLastFlip() {
        return this.flipsLeft.get() == 1;
    }

    /**
     * Flips the hourglass, if not already running, decrements the number of remaining flips
     * and executes the given task once it has finished
     * @param endTask the task to be executed when the hourglass runs out
     * @throws IllegalStateException if the hourglass is already running
     */
    public void flip(Runnable endTask) {
        if (isRunning.compareAndSet(false, true)) {
            if (flipsLeft.get() <= 0) {
                throw new IllegalStateException("The hourglass is already on the last spot");
            }
            scheduledFuture = scheduler.schedule(() -> {
                endTask.run();
                isRunning.set(false);
            }, duration, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("The hourglass is not yet finished");
        }
        flipsLeft.getAndDecrement();
    }
}
