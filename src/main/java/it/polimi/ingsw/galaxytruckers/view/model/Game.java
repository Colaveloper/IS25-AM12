package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.model.factory.GameFactory;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private final Level level;
    private final int playersNumber;

    private final GameFactory gameFactory;
    private final Set<ShipBoard> shipBoards = new HashSet<>();
    private final FlightBoard flightBoard;
    private GameState currentState;

    public Game(Level level, int playersNumber) {
        this.level = level;
        this.playersNumber = playersNumber;
        this.gameFactory = GameFactory.getFactory(level);
        this.flightBoard = this.gameFactory.createFlightBoard();
    }

    /**
     * Sets the game's current state to the given state
     * @param state the {@code GameState} to be set
     */
    public void setCurrentState(GameState state) {
        this.currentState = state;
        state.setGame(this);
    }

    /**
     * @return the game's current state
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * @return the game's flight board
     */
    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    /**
     * @return a {@link Set} containing all the game's ship boards
     */
    public Set<ShipBoard> getShipBoards() {
        return shipBoards;
    }

    /**
     * @return the game's level
     */
    public Level getLevel() {
        return level;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public GameFactory getGameFactory() {
        return gameFactory;
    }
}
