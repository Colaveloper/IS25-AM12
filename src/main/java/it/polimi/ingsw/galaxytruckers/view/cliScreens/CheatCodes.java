package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.utils.Logger;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Utility class that manages cheat codes for testing and debugging purposes.
 * Provides predefined sequences of inputs that can simulate player actions
 * for automated testing and development scenarios.
 */
public class CheatCodes {
    private static final List<String> startToBuild = new ArrayList<>(List.of("0", "", "player1", "C", "SECOND 2"));
    private static final List<String> secondToBuild =new ArrayList<>(List.of("0", "", "player2", "0"));
    private static List<String> cheatString;
    private static boolean isCheatOn;

    static {
        for(int i = 0; i < 20; i++){
            secondToBuild.add("C");
            secondToBuild.add("R");
        }
        secondToBuild.add("e 3");
    }

    /**
     * Activates a specific cheat sequence based on the provided input.
     *
     * @param input The cheat code identifier:
     *              2 - activates the startToBuild sequence
     *              3 - activates the secondToBuild sequence
     */
    public static void activateCheats(int input) {
        isCheatOn = true;
        switch (input) {
            case 2 -> cheatString = startToBuild;
            case 3 -> cheatString = secondToBuild;
        }
    }

    /**
     * Checks if all cheat inputs in the current sequence have been consumed.
     *
     * @return true if there are no more cheat inputs remaining in the sequence,
     *         false if there are still inputs available
     */
    public static boolean cheatEmpty() {
        return cheatString.isEmpty();
    }

    /**
     * Retrieves and removes the next cheat input from the current sequence.
     * Includes a small delay between inputs to simulate real user interaction.
     *
     * @return The next cheat input string from the sequence
     * @throws InterruptedException if the thread sleep is interrupted
     */
    public static String cheat() throws InterruptedException {
        Thread.sleep(200);
        Logger.println(4, cheatString.getFirst());
        return cheatString.removeFirst();
    }

    /**
     * Enables or disables the cheat functionality globally.
     *
     * @param cheat true to enable cheats, false to disable them
     */
    public static void setCheats(boolean cheat) {
        isCheatOn = cheat;
    }

    /**
     * Checks if cheat mode is currently active.
     *
     * @return true if cheats are enabled, false otherwise
     */
    public static boolean isCheatOn() {
        return isCheatOn;
    }
}
