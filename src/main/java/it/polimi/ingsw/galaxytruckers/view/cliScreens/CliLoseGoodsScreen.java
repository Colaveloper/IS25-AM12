package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;

public class CliLoseGoodsScreen extends CliScreen{
    public CliLoseGoodsScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);
    }


    @Override
    public void render() {
        printShips();
        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split("\\s");
        controller.loseGoods(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
    }
}
