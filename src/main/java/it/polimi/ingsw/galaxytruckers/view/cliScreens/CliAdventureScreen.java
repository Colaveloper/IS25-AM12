package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;

public abstract class CliAdventureScreen extends CliScreen{

    protected final boolean isMyTurn;
    protected final boolean imOut;
    protected final ShipBoard currentShip;

    public CliAdventureScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        imOut = gameState.getImOut();
        isMyTurn = gameState.getShipBoard() == myShipBoard;
        currentShip = gameState.getShipBoard();
    }
}
