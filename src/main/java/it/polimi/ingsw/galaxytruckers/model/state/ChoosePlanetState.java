package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ChoosePlanetState extends AdventureState implements GameStateInterface{
    private int shipIndex;
    private final int numPlanets;
    private final List<ShipBoard> orderedShipBoards;
    private final BiConsumer<ShipBoard, Integer> choosePlanetMethod;
    private final ShipBoard[] chosenPlanets;

    public ChoosePlanetState(BiConsumer<ShipBoard,Integer> choosePlanetMethod, int numPlanets) {
        this.choosePlanetMethod = choosePlanetMethod;
        this.numPlanets = numPlanets;
        this.orderedShipBoards = new ArrayList<>();
        this.chosenPlanets = new ShipBoard[numPlanets];
        this.shipIndex = 0;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        this.orderedShipBoards.addAll(game.getFlightBoard().getOrderedShips());
        game.getEventListener().notifyGameStateUpdateEvent(this);
    }

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

    public int getNumPlanets() {
        return numPlanets;
    }

    public synchronized ShipBoard getCurrentShip() {
        return orderedShipBoards.get(shipIndex);
    }

    public synchronized ShipBoard[] getChosenPlanets() {
        return chosenPlanets;
    }
}
