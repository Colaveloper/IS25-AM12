// DESCRIPTION:
// calls the runnable callMeOnFinish after a minute

// USAGE EXAMPLE:
//Hourglass hourglass = new Hourglass();
//hourglass.startTime(() -> System.out.println("Time is up!"));

public class Hourglass {
    public void startTime(Runnable callMeOnFinish) {
        new Thread(() -> {
            try {
                Thread.sleep(60000); // 1 minute
                callMeOnFinish.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
