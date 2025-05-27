package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;

public class CliRemoveCrewScreen extends CliScreen {
    public CliRemoveCrewScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void render() {
        printShips();
        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        if(input.equalsIgnoreCase("X")){
            controller.giveUp();
        }
        else{
            String[] parts = input.split("\\s");
            controller.loseCrew(new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        }
    }
}
