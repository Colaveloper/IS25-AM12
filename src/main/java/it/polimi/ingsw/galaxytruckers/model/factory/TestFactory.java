package it.polimi.ingsw.galaxytruckers.model.factory;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.TestShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.model.state.ShipCorrectionState;
import it.polimi.ingsw.galaxytruckers.model.state.TestShipBuildingState;

import java.io.IOException;

public class TestFactory implements GameFactory{
    @Override
    public Deck createDeck(Game game) throws IOException {
        return new TestDeck(game);
    }

    @Override
    public FlightBoard createFlightBoard(int shipsN, GameEventListener gameEventListener) {
         return new TestFlightBoard(shipsN, gameEventListener);
    }

    @Override
    public ShipBoard createShipBoard(GameColor color, GameEventListener eventListener) {
        return new TestShipBoard(color, eventListener);
    }

    @Override
    public ShipBuildingState createShipBuildingState() {
        return new TestShipBuildingState();
    }

    @Override
    public ShipCorrectionState createShipCorrectionState() {
        return new ShipCorrectionState(false);
    }

    @Override
    public SurrenderPolicy createSurrenderPolicy(GameEventListener gameEventListener) {
        return new NoSurrenderPolicy();
    }

    @Override
    public ScoresRegistry createScoresRegistry() {
        return new ScoresRegistry(new int[]{4,3,2,1},2);
    }
}