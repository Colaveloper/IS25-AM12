package it.polimi.ingsw.galaxytruckers.client.model.factory;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.client.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.ShipBuildingState;

/**
 * Abstract factory for creating game components based on game level.
 */
public abstract class GameFactory {

    /**
     * Creates a flight board appropriate for the game level.
     *
     * @return A FlightBoard configured for the specific game level
     */
    public abstract FlightBoard createFlightBoard();

    /**
     * Creates a ship board appropriate for the game level.
     *
     * @param color The color identifier for the ship board
     * @return A ShipBoard configured for the specific game level
     */
    public abstract ShipBoard createShipBoard(GameColor color);

    /**
     * Creates a ship building state appropriate for the game level.
     *
     * @return A ShipBuildingState configured for the specific game level
     */
    public abstract ShipBuildingState createShipBuildingState();

    /**
     * Factory method to get the appropriate game factory based on difficulty level.
     * - TEST: Returns TestFactory for tutorial gameplay
     * - SECOND: Returns SecondFactory for advanced gameplay
     *
     * @param level The desired game difficulty level
     * @return A GameFactory instance appropriate for the specified level
     * @throws IllegalArgumentException if an unsupported level is provided
     */
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