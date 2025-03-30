package it.polimi.ingsw.galaxytruckers;
// DESCRIPTION:
// calls the runnable callMeOnFinish after a minute

// USAGE EXAMPLE:
//Hourglass hourglass = new Hourglass();
//hourglass.flipHourglass(() -> System.out.println("Time is up!"));

import java.util.concurrent.atomic.AtomicBoolean;

public class Hourglass {
    private int flipsLeft;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public Hourglass(int rounds) {
        this.flipsLeft = rounds;
    }

    public Hourglass() {
        this.flipsLeft = -1;
    }

    public boolean isLastFlip() {
        return this.flipsLeft == 1;
    }

    public void flipHourglass(Runnable hourglassIsOver, Runnable buildingIsOver) {
        if (isRunning.compareAndSet(false, true)) {
            new Thread(() -> {
                try {
                    Thread.sleep(60000); // 1 minute
                    if (flipsLeft == 0 ) {
                        hourglassIsOver.run();
                    } else {
                        buildingIsOver.run();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    isRunning.set(false);
                }
            }).start();

            flipsLeft--;
        }
    }
}
