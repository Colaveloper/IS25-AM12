package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveCrewState;

import java.awt.*;

public class CliRemoveCrewScreen extends CliScreen {

    private RemoveCrewState gameState;
    public CliRemoveCrewScreen(ClientModel model, ControllerToServer controller, RemoveCrewState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
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
