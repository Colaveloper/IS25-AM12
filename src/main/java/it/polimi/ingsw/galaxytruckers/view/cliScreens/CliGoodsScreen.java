package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.io.IOException;
import java.util.List;

public class CliGoodsScreen extends CliScreen {

    public CliGoodsScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);
    }


    @Override
    public void render() {
        printShips();

        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {

    }


}
