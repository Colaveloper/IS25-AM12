package it.polimi.ingsw.galaxytruckers.client.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implements a timer mechanism that simulates an hourglass in the Galaxy Truckers game.
 * This class manages countdown timer functionality with a fixed duration for each flip,
 * tracks remaining flips, and handles concurrent timer operations in a thread-safe manner.
 */
public class Hourglass {
    /** The duration in seconds for each flip of the hourglass */
    private final static long DURATION = 60;

    /** Number of remaining flips available for this hourglass */
    private int flipsLeft;

    /** Thread-safe flag indicating whether the hourglass timer is currently running */
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    /** Scheduled executor service for timer operations */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /** Reference to the currently scheduled timer task */
    private ScheduledFuture<?> scheduledFuture;

    /** Thread-safe counter for the remaining time in seconds */
    private final AtomicLong timeLeft = new AtomicLong(0);

    /**
     * Creates a new hourglass with the specified number of available flips.
     *
     * @param rounds The number of times the hourglass can be flipped
     */
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

    /**
     * Stops the hourglass timer immediately, canceling any scheduled tasks.
     * This method is safe to call even if the timer is not running.
     */
    public void end() {
        isRunning.set(false);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    /**
     * Reconfigures the hourglass with new settings.
     * Stops any current timer before applying the new settings.
     * If the isRunning parameter is true, starts the timer with the new settings.
     *
     * @param flipsLeft The new number of remaining flips
     * @param timeLeft The new time left in seconds
     * @param isRunning Whether the hourglass should be running after setup
     */
    public void setup(int flipsLeft, int timeLeft, boolean isRunning) {
        end();
        this.flipsLeft = flipsLeft;
        this.timeLeft.set(timeLeft);
        if (isRunning) start();
    }

    /**
     * Starts the hourglass timer.
     * Schedules a task that decrements the time left every second.
     * When the time reaches zero, the timer automatically stops.
     */
    private void start() {
        isRunning.set(true);
        scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            long currentTime = timeLeft.decrementAndGet();
            if (currentTime < 0) {
                end();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Gets the number of flips remaining for this hourglass.
     *
     * @return The number of remaining flips
     */
    public int getFlipsLeft() {
        return flipsLeft;
    }

    /**
     * Checks if the hourglass timer is currently running.
     *
     * @return true if the timer is running, false otherwise
     */
    public boolean getIsRunning() {
        return isRunning.get();
    }

    /**
     * Gets the remaining time in seconds.
     *
     * @return The time left in seconds
     */
    public long getTimeLeft() {
        return timeLeft.get();
    }
}
