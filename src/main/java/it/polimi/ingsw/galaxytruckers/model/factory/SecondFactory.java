package it.polimi.ingsw.galaxytruckers.model.factory;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.SecondShipBuildingState;

import java.io.IOException;

public class SecondFactory extends GameFactory{
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
    public GameState createFirstGameState() {
        return new SecondShipBuildingState();
    }
}