package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.factory.GameFactory;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.EndGameState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game {
    private final GameFactory gameFactory;
    private final Set<ShipBoard> shipBoards = new HashSet<>();
    private FlightBoard flightBoard;
    private Set<ShipBoard> givenUpShips = new HashSet<>();
    private Deck deck;
    private GameState currentState;
    private Map<ShipBoard, Integer> finalScores;
    Level level;

    public Game(Level level) {
        this.level = level;
        this.gameFactory = GameFactory.getFactory(level);
    }

    /**
     * Adds shipboard of the given color to the game
     * @param color the color of the added shipboard
     * @return the added shipboard
     */
    public ShipBoard addShipBoard(Colors color) {
        ShipBoard shipBoard = gameFactory.createShipBoard(color);
        shipBoards.add(shipBoard);
        return shipBoard;
    }

    /**
     * Instantiates the game's flightBoard and Deck and sets the
     * current state of the game to shipbuilding
     * @throws IOException if an error occurs when trying to load the deck's
     * cards from disk
     */
    public void start() throws IOException{
        this.flightBoard = gameFactory.createFlightBoard(shipBoards);
        this.deck = gameFactory.createDeck();
        setCurrentState(gameFactory.createFirstGameState());
    }

    /**
     * @return the game's factory
     */
    public GameFactory getGameFactory() {
        return gameFactory;
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
     * @return the game's deck
     */
    public Deck getDeck() {
        return deck;
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

    public void forceShipsToGiveUp(){
        Set<ShipBoard> shipsToGiveUp = new HashSet<>();
        // if crew == 0, or you have no engine power -> give up
        for (ShipBoard s : shipBoards){
            if(s.getEnginePower() == 0 || s.getCrewSize() == 0)
                givenUpShips.add(s);
        }

        // if you get lapped -> give up
        givenUpShips.addAll(flightBoard.getAndRemoveLappedShips());

        // make sure these ships are removed from flightboard
        flightBoard.removeShips(givenUpShips);
    }

    public Set<ShipBoard> getGivenUpShips(){return givenUpShips;}

    public void endGame(){
        finalScores = flightBoard.getFinalScores();
        GameState endState = new EndGameState(finalScores);
        setCurrentState(endState);
    }

    public Map<ShipBoard, Integer> getFinalScores(){
        return finalScores;
    }
}
