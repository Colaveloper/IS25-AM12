package it.polimi.ingsw.galaxytruckers.view.model.factory;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

public abstract class GameFactory {
    public abstract FlightBoard createFlightBoard();
    public abstract ShipBoard createShipBoard(FourColors color);
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