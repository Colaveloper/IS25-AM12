package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.model.factory.GameFactory;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private List<ModelObserver> observers;

    private final Level level;

    private final GameFactory gameFactory;
    private final Set<ShipBoard> shipBoards = new HashSet<>();
    private final Set<ShipBoard> givenUpShips = new HashSet<>();
    private final FlightBoard flightBoard;
    private GameState currentState = null;

    public Game(Level level) {
        this.level = level;
        this.gameFactory = GameFactory.getFactory(level);
        this.flightBoard = this.gameFactory.createFlightBoard();
    }

    public void setObservers(List<ModelObserver> observers) {
        this.observers = observers;
    }

    public List<ModelObserver> getObservers() {
        return observers;
    }

    public ShipBoard addShipBoard(GameColor color) {
        ShipBoard shipBoard = gameFactory.createShipBoard(color);
        shipBoards.add(shipBoard);
        return shipBoard;
    }

    /**
     * Sets the game's current state to the given state
     * @param state the {@code GameState} to be set
     */
    public void setCurrentState(GameState state) {
        if (this.currentState != null) this.currentState.leave();
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

    public void setGivenUpShips(Set<ShipBoard> givenUpShips) {
        this.givenUpShips.addAll(givenUpShips);
    }

    public Set<ShipBoard> getGivenUpShips(){
        return givenUpShips;
    }

    public Set<GameColor> getShipColors(){
        Set<GameColor> colors = new HashSet<>();
        for(ShipBoard s : shipBoards){
            colors.add(s.getColor());
        }
        return colors;
    }

    /**
     * @return the game's level
     */
    public Level getLevel() {
        return level;
    }

    public GameFactory getGameFactory() {
        return gameFactory;
    }

}
