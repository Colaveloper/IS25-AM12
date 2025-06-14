package it.polimi.ingsw.galaxytruckers.model.factory;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.SurrenderPolicy;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.model.state.ShipCorrectionState;

import java.io.IOException;

public interface GameFactory {
    Deck createDeck(Game game) throws IOException;
    FlightBoard createFlightBoard(int shipsN);
    ShipBoard createShipBoard(GameColor color);
    ShipBuildingState createShipBuildingState();
    ShipCorrectionState createShipCorrectionState();
    SurrenderPolicy createSurrenderPolicy();


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