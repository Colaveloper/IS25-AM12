package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.enums.GoodsType;
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
        System.out.println("To grab one of the goods, type the point on the ship where\n" +
                "you wish to place the good, followed by the type of good\n" +
                "e.g. 5 6 yellow\n");
        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split("\\s");
        controller.placeGoods(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])), GoodsType.valueOf(parts[2].toUpperCase()));
    }
}
