package it.polimi.ingsw.galaxytruckers.server.model;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.server.model.state.ShipCorrectionState;

import java.io.IOException;

/**
 * Factory interface for creating objects, states and behaviors that have level-specificity.
 */
public interface GameFactory {
    /**
     * Creates a new deck for the game.
     *
     * @param game the game for which the deck is created
     * @return a new Deck instance
     */
    Deck createDeck(Game game) throws IOException;

    /**
     * Creates a new FlightBoard for the game.
     * @param shipsN the number of ships in the game
     * @param gameEventListener the listener for game events
     * @return a new FlightBoard instance
     */
    FlightBoard createFlightBoard(int shipsN, GameEventListener gameEventListener);

    /**
     * Creates a new ShipBoard for the game.
     *
     * @param color the color of the ship
     * @param eventListener the listener for ship events
     * @return a new ShipBoard instance
     */
    ShipBoard createShipBoard(GameColor color, GameEventListener eventListener);

    /**
     * Creates a new {@link it.polimi.ingsw.galaxytruckers.client.model.state.ShipBuildingState} instance.
     *
     * @return a new ShipBuildingState instance
     */
    ShipBuildingState createShipBuildingState();

    /**
     * Creates a new {@link ShipCorrectionState} instance.
     *
     * @return a new ShipCorrectionState instance
     */
    ShipCorrectionState createShipCorrectionState();

    /**
     * Creates a new SurrenderPolicy for the game.
     *
     * @param gameEventListener the listener for game events
     * @return a new SurrenderPolicy instance
     */
    SurrenderPolicy createSurrenderPolicy(GameEventListener gameEventListener);

    /**
     * Creates a new ScoresRegistry for the game.
     *
     * @return a new ScoresRegistry instance
     */
    ScoresRegistry createScoresRegistry();


    /**
     * Returns a GameFactory instance based on the specified game level.
     * @param level the level of the game for which the factory is requested
     * @return a GameFactory instance corresponding to the specified level
     */
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