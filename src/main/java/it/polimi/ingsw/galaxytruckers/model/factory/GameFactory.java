package it.polimi.ingsw.galaxytruckers.model.factory;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.model.state.ShipCorrectionState;

import java.io.IOException;

public interface GameFactory {
    Deck createDeck(Game game) throws IOException;
    FlightBoard createFlightBoard(int shipsN, GameEventListener gameEventListener);
    ShipBoard createShipBoard(GameColor color, GameEventListener eventListener);
    ShipBuildingState createShipBuildingState();
    ShipCorrectionState createShipCorrectionState();
    SurrenderPolicy createSurrenderPolicy(GameEventListener gameEventListener);
    ScoresRegistry createScoresRegistry();


    static GameFactory getFactory(Level level) {
        GameFactory gameFactory = null;
        switch (level) {
            case TEST -> gameFactory = new TestFactory();
            case SECOND -> gameFactory = new SecondFactory();
            default -> throw new IllegalArgumentException("Unexpected level: " + level);
        }
        return gameFactory;
    }
}