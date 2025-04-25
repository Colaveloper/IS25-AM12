package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Arrays;

public class GameCreationScreen implements ScreenStrategy {
    @Override
    public void showCLI(ClientModel model) {
        System.out.println("No one is currently creating a new game, so... it's up to you!");
        System.out.println("Choose a Level and the number of players");
        System.out.print("Available levels: ");
        System.out.println(String.join(", ", Arrays.stream(Level.values())
                .map(Enum::name)
                .toArray(String[]::new)));
        System.out.println("The game is available for 2, 3, or 4 players");
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        for (Level level : Level.values()) {
            if (input.matches("^" + level.name() + "\\s[2-4]$")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
        String[] parts = input.split("\\s");
        server.newGame(Level.valueOf(parts[0].toUpperCase()), Integer.parseInt(parts[1]));
    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) {

    }
}
