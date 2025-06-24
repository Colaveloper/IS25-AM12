package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.factory.GameFactory;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a game session in the Galaxy Truckers game.
 * This class manages the game state, player ships, flight board,
 * and other components required for gameplay. It's instantiated with
 * a specific flight level, test or level II that determines how the game components
 * are configured.
 */
public class Game {
    /** List of observers that will be notified of game state changes */
    private List<ModelObserver> observers;

    /** The flight level of this game */
    private final Level level;

    /** Factory for creating game components based on the level */
    private final GameFactory gameFactory;

    /** Set of all ship boards in the game */
    private final Set<ShipBoard> shipBoards = new HashSet<>();

    /** Set of ships that have given up during the game */
    private final Set<ShipBoard> givenUpShips = new HashSet<>();

    /** The flight board where ships move during gameplay */
    private final FlightBoard flightBoard;

    /** The currently active adventure card */
    private AdventureCard currentCard;

    /** The ship board associated with the local player */
    private ShipBoard myShip;

    /** The current state of the game (building, flight, etc.) */
    private GameState currentState = null;

    /**
     * Creates a new game with the specified difficulty level.
     * Initializes the game factory and flight board based on the level.
     *
     * @param level The difficulty level for this game
     */
    public Game(Level level) {
        this.level = level;
        this.gameFactory = GameFactory.getFactory(level);
        this.flightBoard = this.gameFactory.createFlightBoard();
    }

    /**
     * Sets the list of observers that will be notified of game state changes.
     *
     * @param observers The list of model observers
     */
    public void setObservers(List<ModelObserver> observers) {
        this.observers = observers;
    }

    /**
     * Gets the list of observers for this game.
     *
     * @return The list of model observers
     */
    public List<ModelObserver> getObservers() {
        return observers;
    }

    /**
     * Sets the local player's ship board.
     *
     * @param myShip The ship board associated with the local player
     */
    public void setMyShip(ShipBoard myShip) {
        this.myShip = myShip;
    }

    /**
     * Creates a new ship board with the specified color and adds it to the game.
     *
     * @param color The color for the new ship board
     * @return The newly created ship board
     */
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
     * Gets the ship board associated with the local player.
     *
     * @return The local player's ship board
     */
    public ShipBoard getMyShip() {
        return myShip;
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
     * Adds ships to the set of ships that have given up.
     *
     * @param givenUpShips The set of ships to mark as having given up
     */
    public void setGivenUpShips(Set<ShipBoard> givenUpShips) {
        this.givenUpShips.addAll(givenUpShips);
    }

    /**
     * Gets the set of ships that have given up during the game.
     *
     * @return Set of ship boards that have surrendered
     */
    public Set<ShipBoard> getGivenUpShips(){
        return givenUpShips;
    }

    /**
     * Gets the set of colors used by ships in the game.
     *
     * @return Set of game colors currently in use
     */
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

    /**
     * Gets the game factory used to create game components.
     *
     * @return The game factory for this game
     */
    public GameFactory getGameFactory() {
        return gameFactory;
    }

    /**
     * Gets the currently active adventure card.
     *
     * @return The current adventure card
     */
    public AdventureCard getCurrentCard() {
        return currentCard;
    }

    /**
     * Sets the currently active adventure card.
     *
     * @param currentCard The adventure card to set as current
     */
    public void setCurrentCard(AdventureCard currentCard) {
        this.currentCard = currentCard;
    }
}
