package it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;

import java.util.Arrays;

/**
 * CLI screen for creating a new game in the Galaxy Truckers game.
 */
public class CliGameCreationScreen extends CliScreen {

    /**
     * Creates a new game creation screen with the given model and controller.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     */
    public CliGameCreationScreen(ClientModel model, ClientControllerInterface controller) {
        super(model, controller);
    }

    @Override
    public void parseAndInvoke(String input) {
        if (input.trim().isEmpty()) {
            input = "SECOND 2";
        }
        String[] parts = input.split("\\s");
        controller.requestNewGame(Level.valueOf(parts[0].toUpperCase()), Integer.parseInt(parts[1]));
    }

    @Override
    public boolean isInputLegal(String input) {
        if (input.trim().isEmpty()) {
            return true;
        }
        for (Level level : Level.values()) {
            if (input.toUpperCase().matches("^" + level.name() + "\\s[2-4]$")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render() {
        System.out.println("No one is currently creating a new game, so... it's up to you!");
        System.out.println("Choose a Level and the number of players");
        System.out.println("Available levels: " + String.join(", ",
                Arrays.stream(Level.values())
                        .map(Enum::name)
                        .toArray(String[]::new)));
        System.out.println("The game is available for 2, 3, or 4 players");
        System.out.println("Default: SECOND 2");
    }
}
