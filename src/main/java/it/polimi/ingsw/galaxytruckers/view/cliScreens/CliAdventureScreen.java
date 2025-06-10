package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;

import java.util.ArrayList;
import java.util.List;

public abstract class CliAdventureScreen extends CliScreen{

    protected final boolean isMyTurn;
    protected final boolean imOut;
    protected final ShipBoard currentShip;

    public CliAdventureScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        imOut = gameState.getImOut();   //if player surrendered
        currentShip = gameState.getShipBoard(); // ship of the current player playing
        isMyTurn = currentShip == myShipBoard;

    }

//    protected List<String> printShipFlightStats() {
//        List<String> description = new ArrayList<>();
//
//        description.addAll(cliFlightBoard.getDescription());
//        description.add(
//            "firepower: "   + myShipBoard.getFirePower() +
//            "\tengine power: " + myShipBoard.getEnginePower() +
//            "\tbatteries: "   + myShipBoard.getNumBatteries() +
//            "\tcrewsize: "    + myShipBoard.getCrewSize()
//        );
//        description.addAll(cliAllShips.getDescription());
//        return description;
//    }
}
