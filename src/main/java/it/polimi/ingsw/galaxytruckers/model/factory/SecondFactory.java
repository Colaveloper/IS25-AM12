package it.polimi.ingsw.galaxytruckers.model.factory;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.SecondShipBuildingState;

import java.io.IOException;
import java.util.Set;

public class SecondFactory extends GameFactory{
    @Override
    public Deck createDeck() throws IOException {
        return new SecondDeck();
    }

    @Override
    public FlightBoard createFlightBoard(Set<ShipBoard> allShips) {
        return new SecondFlightBoard(allShips);
    }

    @Override
    public ShipBoard createShipBoard(FourColors color) {
         return new SecondShipBoard(color);
    }

    @Override
    public GameState createFirstGameState() {
        return new SecondShipBuildingState();
    }
}