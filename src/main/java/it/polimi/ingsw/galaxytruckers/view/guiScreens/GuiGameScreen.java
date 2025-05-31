package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAllShips;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

public abstract class GuiGameScreen extends GuiScreen {

    protected GuiFlightBoard guiFlightBoard;
    protected GuiAllShips guiAllShips;
//    protected ShipBoard myShipBoard; //?
//    protected Map<ShipBoard, CliShipHandAndStash> shipToCliShip; //?

    public GuiGameScreen(ClientModel model, ControllerToServer controller, GameState state) {
        super(model, controller, state);
        //        this.myShipBoard = model.getMyShip();
//        this.shipToCliShip = new HashMap<>();
//        for (Player player : model.getPlayers()) {
//            shipToCliShip.put(player.getShipBoard(), new CliShipHandAndStash(player.getShipBoard(), player.getNickname()));
//        }
        this.guiAllShips = new GuiAllShips(model.getClientPlayer(), model.getShipToPlayer(), controller);
        this.guiFlightBoard = new GuiFlightBoard(model.getGame().getFlightBoard(), controller);
    }
}
