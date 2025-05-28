package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheatCodes {
    private static List<String> startToBuild = new ArrayList<>(List.of("0", "", "player1", "C", ""));
    private static List<String> secondToBuild =new ArrayList<>(List.of("0", "", "player2"));
    private static List<String> cheatString;

    public static void activateCheats(int input) {
        switch (input) {
            case 2 -> cheatString = startToBuild;
            case 3 -> cheatString = secondToBuild;
        }
    }

    public static boolean cheatEmpty() {
        return cheatString.isEmpty();
    }

    public static String cheat() throws InterruptedException {
        Thread.sleep(100);//da togliere dopo aver sistemato la syn perchè ora non funziona senza
        return cheatString.removeFirst();
    }
}
