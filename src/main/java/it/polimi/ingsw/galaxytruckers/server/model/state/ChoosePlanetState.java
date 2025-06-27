package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Represents the state of the game where players choose planets to land on.
 * This state is part of the adventure phase where players select planets to land on.
 */
public final class ChoosePlanetState extends AdventureState implements GameStateInterface{
    private int shipIndex;
    private final int numPlanets;
    private final List<ShipBoard> orderedShipBoards;
    private final BiConsumer<ShipBoard, Integer> choosePlanetMethod;
    private final ShipBoard[] chosenPlanets;

    /**
     * Constructor for ChoosePlanetState.
     * @param choosePlanetMethod the method to call when a planet is chosen
     * @param numPlanets the number of planets to choose from
     */
    public ChoosePlanetState(BiConsumer<ShipBoard,Integer> choosePlanetMethod, int numPlanets) {
        this.choosePlanetMethod = choosePlanetMethod;
        this.numPlanets = numPlanets;
        this.orderedShipBoards = new ArrayList<>();
        this.chosenPlanets = new ShipBoard[numPlanets];
        this.shipIndex = 0;
    }

    /**
     * Skips the current player if it's their turn.
     * @param shipBoard the ship board of the player who wants to skip
     */
    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && shipBoard.equals(getCurrentShip())) {
            ShipBoard nextShipBoard = nextShip();
            if (nextShipBoard != null) {
                game.getEventListener().notifyCurrentPlayerUpdateEvent(nextShipBoard);
            }
        }
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        this.orderedShipBoards.addAll(game.getFlightBoard().getOrderedShips());
    }

    /**
     * Allows the player to choose a planet by calling the provided method.
     * Moves to the next ship on the flight board after the choice is made.
     *
     * @param shipBoard the ship board of the player
     * @param choice    the option representing the chosen planet
     * @throws IllegalStateException if it's not the player's turn
     */
    @Override
    public synchronized void choosePlanet(ShipBoard shipBoard, int choice) {
        if (!shipBoard.equals(getCurrentShip())) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        if (choice < 0 || choice > this.numPlanets || chosenPlanets[choice] != null) {
            throw new IllegalArgumentException("Invalid choice: " + choice);
        }
        choosePlanetMethod.accept(getCurrentShip(),choice);
        chosenPlanets[choice] = shipBoard;
        ShipBoard nextShipBoard = nextShip();
        if (nextShipBoard != null) {
            game.getEventListener().notifyPlanetChoiceEvent(shipBoard,choice, nextShipBoard);
        }
    }

    /**
     * Moves to the next ship on the flight board.
     *
     * @param shipBoard the ship board of the player that requested the action
     */
    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(orderedShipBoards.get(shipIndex))) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        ShipBoard nextShipBoard = nextShip();
        if (nextShipBoard != null)
            game.getEventListener().notifyCurrentPlayerUpdateEvent(nextShipBoard);
    }

    private ShipBoard nextShip() {
        shipIndex++;
        if (shipIndex >= orderedShipBoards.size()) {
            getNextState();
            return null;
        }
        return orderedShipBoards.get(shipIndex);
    }

    /**
     * Returns the number of planets that can be chosen in this state.
     *
     * @return the number of planets
     */
    public int getNumPlanets() {
        return numPlanets;
    }

    /**
     * Returns the current ship board of the player whose turn it is.
     *
     * @return the current ship board
     */
    public synchronized ShipBoard getCurrentShip() {
        return orderedShipBoards.get(shipIndex);
    }

    /**
     * @return an array containing at each position the ship board of the player
     * that has chosen that planet or null if no player has chosen that planet yet.
     */
    public synchronized ShipBoard[] getChosenPlanets() {
        return chosenPlanets;
    }
}
