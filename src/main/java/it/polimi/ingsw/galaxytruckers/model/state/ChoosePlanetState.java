package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.*;
import java.util.function.Consumer;

public final class ChoosePlanetState extends AdventureState implements GameStateInterface{
    private int shipIndex;
    private final int numPlanets;
    private final List<ShipBoard> orderedShipBoards;
    private final Consumer<Integer> choosePlanetMethod;
    private final Map<ShipBoard, Integer> shipToChoice;
    private final boolean[] chosenPlanets;

    public ChoosePlanetState(Consumer<Integer> choosePlanetMethod, int numPlanets) {
        this.choosePlanetMethod = choosePlanetMethod;
        this.numPlanets = numPlanets;
        this.shipToChoice = new HashMap<>();
        this.orderedShipBoards = new ArrayList<>();
        this.chosenPlanets = new boolean[numPlanets];
        this.shipIndex = 0;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        this.orderedShipBoards.addAll(game.getFlightBoard().getOrderedShips());
        game.getEventListener().notifyGameStateUpdateEvent(this);
    }

    @Override
    public void choosePlanet(ShipBoard shipBoard, int choice) {
        if (!shipBoard.equals(orderedShipBoards.get(shipIndex))) {
            throw new IllegalStateException("It's not your turn");
        }
        if (choice < 0 || choice > this.numPlanets || chosenPlanets[choice]) {
            throw new IllegalArgumentException("Invalid choice: " + choice);
        }
        choosePlanetMethod.accept(choice);
        shipToChoice.put(shipBoard,choice);
        chosenPlanets[choice] = true;
        ShipBoard nextShipBoard = nextShip();
        if (nextShipBoard != null) {
            game.getEventListener().notifyPlanetChoiceEvent(shipBoard,choice, nextShipBoard);
        }
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        if (!shipBoard.equals(orderedShipBoards.get(shipIndex))) {
            throw new IllegalStateException("It's not your turn");
        }
        nextShip();
    }

    private ShipBoard nextShip() {
        shipIndex++;
        if (shipIndex >= orderedShipBoards.size()) {
            game.setCurrentState(getNextState());
            return null;
        }
        return orderedShipBoards.get(shipIndex);
    }

    public int getNumPlanets() {
        return numPlanets;
    }

    public ShipBoard getCurrentShip() {
        return orderedShipBoards.get(shipIndex);
    }
}
