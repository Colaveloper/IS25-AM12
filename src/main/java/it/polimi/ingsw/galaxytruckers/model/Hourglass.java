package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Hourglass {
    private final static int DURATION = 60000; // 60 seconds in milliseconds
    private final static int period = 100; // 100 milliseconds

    private int flipsLeft;
    private boolean isRunning = false;
    private int missingTime = 0;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private int duration = DURATION;

    public Hourglass(int rounds) {
        this.flipsLeft = rounds;
    }

    @VisibleForTesting
    public synchronized void setDuration(int duration) {
        this.duration = duration;
    }

    public synchronized void stop() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            missingTime = 0;
            isRunning = false;
        }
    }

    /**
     * @return {@code true} if the next hourglass flip is the last one,
     * {@code false} otherwise
     */
    public synchronized boolean isLastFlip() {
        return this.flipsLeft == 1;
    }

    /**
     * Flips the hourglass, if not already running, decrements the number of remaining flips
     * and executes the given task once it has finished
     * @param endTask the task to be executed when the hourglass runs out
     * @throws IllegalStateException if the hourglass is already running
     */
    public synchronized void flip(Runnable endTask) {
        if (!isRunning) {
            if (flipsLeft <= 0) {
                throw new IllegalStateException("The hourglass is already on the last spot");
            }
            isRunning = true;
            missingTime = duration;
            scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
                boolean end = false;
                synchronized (this) {
                    missingTime-= period; // Decrement by 100 milliseconds
                    if (missingTime <= 0) {
                        end = true;
                        isRunning = false;
                    }
                }
                if (end) {
                    endTask.run();
                    stop();
                }
            }, 0, period, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("The hourglass is not yet finished");
        }
        flipsLeft--;
    }

    public synchronized int getFlipsLeft() {
        return flipsLeft;
    }

    public synchronized boolean getIsRunning() {
        return isRunning;
    }

    public synchronized int getMissingTime() {
        return missingTime;
    }
}
