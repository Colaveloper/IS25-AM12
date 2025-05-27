package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
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
        String[] parts = input.split("\\s");
        if (parts[0].equalsIgnoreCase("P")){
            controller.placeGoods(new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])), GoodsType.valueOf(parts[3].toUpperCase()));
        }
        if (parts[0].equalsIgnoreCase("R")){
            controller.removeGoods(new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])), GoodsType.valueOf(parts[3].toUpperCase()));
        }
    }
}
