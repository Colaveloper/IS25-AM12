package it.polimi.ingsw.galaxytruckers.model.factory;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.SecondShipBuildingState;
import it.polimi.ingsw.galaxytruckers.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.model.state.ShipCorrectionState;

import java.io.IOException;

public class SecondFactory implements GameFactory{
    @Override
    public Deck createDeck(Game game) throws IOException {
        SecondDeck deck = new SecondDeck(game);
        deck.initMasterDeck();
        return deck;
    }

    @Override
    public FlightBoard createFlightBoard(int shipsN) {
        return new SecondFlightBoard(shipsN);
    }

    @Override
    public ShipBoard createShipBoard(GameColor color) {
         return new SecondShipBoard(color);
    }

    @Override
    public ShipBuildingState createShipBuildingState() {
        return new SecondShipBuildingState();
    }

    @Override
    public ShipCorrectionState createShipCorrectionState() {
        return new ShipCorrectionState(true);
    }

    @Override
    public SurrenderPolicy createSurrenderPolicy() {
        return new EnabledSurrenderPolicy();
    }
}