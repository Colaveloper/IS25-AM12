package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

public class Game {
    GameFactory gameFactory;
    ComponentBank componentBank;
    Set<ShipBoard> shipBoards;
    FlightBoard flightBoard;
    Deck deck;
    GameState currentState;

    public Game(Level level) {
        switch(level) {
            case TEST -> this.gameFactory = new TestFactory();
            case SECOND -> this.gameFactory = new SecondFactory();
            default -> throw new IllegalArgumentException("Level not supported");
        }
        this.componentBank = new ComponentBank();
    }

    public ShipBoard addShipBoard(Colors color) {
        ShipBoard shipBoard = gameFactory.createShipBoard(componentBank, color);
        shipBoards.add(shipBoard);
        return shipBoard;
    }

    public void start() {
        this.flightBoard = gameFactory.createFlightBoard(shipBoards);
        try {
            this.deck = gameFactory.createDeck();
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot create deck because an exception was thrown:\n", exception);
        }
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
