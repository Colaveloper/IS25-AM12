package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.ShipBuildingState;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Game {
    private final GameFactory gameFactory;
    private final Set<ShipBoard> shipBoards;
    private FlightBoard flightBoard;
    private Deck deck;
    private GameState currentState;

    public Game(Level level) {
        switch(level) {
            case TEST -> this.gameFactory = new TestFactory();
            case SECOND -> this.gameFactory = new SecondFactory();
            default -> throw new IllegalArgumentException("Level not supported");
        }
        this.shipBoards = new HashSet<>();
    }

    public ShipBoard addShipBoard(Colors color) {
        ShipBoard shipBoard = gameFactory.createShipBoard(color);
        shipBoards.add(shipBoard);
        return shipBoard;
    }

    public void start() {
        this.flightBoard = gameFactory.createFlightBoard(shipBoards);
        try {
            this.deck = gameFactory.createDeck();
        } catch (IOException exception) {
            throw new RuntimeException("Cannot create deck because an exception was thrown:\n", exception);
        }
        setCurrentState(new ShipBuildingState());
    }

    public GameFactory getGameFactory() {
        return gameFactory;
    }

    public void setCurrentState(GameState state) {
        this.currentState = state;
        state.setGame(this);
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public Deck getDeck() {
        return deck;
    }

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public Set<ShipBoard> getShipBoards() {
        return shipBoards;
    }
}
