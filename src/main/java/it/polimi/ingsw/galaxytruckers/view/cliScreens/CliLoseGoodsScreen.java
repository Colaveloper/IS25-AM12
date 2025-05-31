package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveGoodsState;

import java.awt.*;

public class CliLoseGoodsScreen extends CliScreen{

    private RemoveGoodsState gameState;

    public CliLoseGoodsScreen(ClientModel model, ControllerToServer controller, RemoveGoodsState gameState) {
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
        String[] parts = input.split("\\s");
        if (parts[0].equalsIgnoreCase("X")){
            controller.giveUp();
        }
        else{
            controller.loseGoods(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
    }

    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType){}

    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType){}
}
