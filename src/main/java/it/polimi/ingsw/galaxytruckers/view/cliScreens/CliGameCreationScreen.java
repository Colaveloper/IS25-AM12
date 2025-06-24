package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CliGameCreationScreen extends CliScreen {

    public CliGameCreationScreen(ClientModel model, ControllerToServer controller) {
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
