package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GrabRewardState;

public class CliRewardScreen extends CliScreen {

    private GrabRewardState gameState;
    public CliRewardScreen(ClientModel model, ControllerToServer controller, GrabRewardState gameState) {
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
        else {
            controller.grabReward(!input.isEmpty());
        }
    }
}
