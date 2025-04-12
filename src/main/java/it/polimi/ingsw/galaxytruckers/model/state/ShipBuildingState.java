package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// TODO: move logic for requesting components to this state,
//  instantiate ComponentBank here instead of game
public class ShipBuildingState extends GameState {
    Set<ShipBoard> completedShipBoards;
    Hourglass hourglass;
    Map<ShipBoard, Integer> shipToForecasts;
    Set<Integer> blockedForecasts;

    public ShipBuildingState() {
        this.completedShipBoards = new HashSet<>();
    }

    @Override
    public void requestRandComponent(ShipBoard shipBoard) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.requestRandComponent();
    }

    @Override
    public void requestComponent(ShipBoard shipBoard, int componentId) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.requestComponent(componentId);  //TODO: define componentIdentifiers
    }

    @Override
    public void rejectComponent(ShipBoard shipBoard) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.rejectComponent();
    }

    @Override
    public void stashComponent(ShipBoard shipBoard) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.stashComponent();
    }

    @Override
    public void grabStashedComponent(ShipBoard shipBoard, int index) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.grabStashedComponent(index);
    }

    @Override
    public void placeComponent(ShipBoard shipBoard, Point point, int orientation) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        //shipBoard.placeComponent(point, orientation);
        //TODO : uncomment once shipBoard is fixed
    }

    @Override
    public void flipHourglass(ShipBoard shipBoard) {
        if (hourglass.isLastFlip() && !completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship must be completed before the last flip");
        }
        //TODO: handle flipping with state transition and just notify
    }

    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        game.getFlightBoard().placeShipOnFlightBoard(shipBoard, startingPosition);
        completedShipBoards.add(shipBoard);
    }

    @Override
    public void acquireForecast(ShipBoard shipBoard, int deckIndex) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        if (blockedForecasts.contains(deckIndex)) {
            throw new IllegalArgumentException("Deck is unavailable");
        }
        game.getDeck().getForecastDeck(deckIndex);
        blockedForecasts.add(deckIndex);
        shipToForecasts.put(shipBoard, deckIndex);
    }

    @Override
    public void releaseForecast(ShipBoard shipBoard) {
        if (shipToForecasts.containsKey(shipBoard)) {
            int index = shipToForecasts.remove(shipBoard);
            blockedForecasts.remove(index);
        }
    }
}
