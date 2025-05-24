package it.polimi.ingsw.galaxytruckers.model.factory;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.io.IOException;
import java.util.Set;

public abstract class GameFactory {
    public abstract Deck createDeck(Game game) throws IOException;
    public abstract FlightBoard createFlightBoard(Set<ShipBoard> allShips);
    public abstract ShipBoard createShipBoard(GameColor color);
    public abstract GameState createFirstGameState();

    public static GameFactory getFactory(Level level) {
        GameFactory gameFactory = null;
        switch (level) {
            case TEST -> gameFactory = new TestFactory();
            case SECOND -> gameFactory = new SecondFactory();
            default -> throw new IllegalArgumentException("Unexpected level: " + level);
        }
        return gameFactory;
    }
}