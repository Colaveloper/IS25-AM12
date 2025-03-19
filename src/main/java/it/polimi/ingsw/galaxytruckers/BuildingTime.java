package it.polimi.ingsw.galaxytruckers;// DESCRIPTION:
// calls the runnable callMeOnFinish after a minute

// USAGE EXAMPLE:
//Hourglass hourglass = new Hourglass();
//hourglass.flipHourglass(() -> System.out.println("Time is up!"));

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class BuildingTime {
    private int flipsLeft;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public BuildingTime(Optional<Integer> rounds) {
        this.flipsLeft = rounds.orElse(-1);
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
