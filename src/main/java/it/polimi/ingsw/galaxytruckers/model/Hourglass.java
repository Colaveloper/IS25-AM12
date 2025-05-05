package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Hourglass {
    private final static long DURATION = 60000;

    private int flipsLeft;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final Timer timer = new Timer();
    private long duration = DURATION;

    public Hourglass(int rounds) {
        this.flipsLeft = rounds;
    }

    public Hourglass() {
        this.flipsLeft = -1;
    }

    @VisibleForTesting
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
     * @param endTask the task to be executed when the hourglass runs out
     * @throws IllegalStateException if the hourglass is already running
     */
    public void flip(Runnable endTask) {
        if (isRunning.compareAndSet(false, true)) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    endTask.run();
                    isRunning.set(false);
                }
            }, duration);
        } else {
            throw new IllegalStateException("The hourglass is not yet finished");
        }
        flipsLeft--;
    }
}
