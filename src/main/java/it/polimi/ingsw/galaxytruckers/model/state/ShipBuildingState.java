package it.polimi.ingsw.galaxytruckers.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShipBuildingState extends GameState {
    private final ComponentBank componentBank;
    private final Set<ShipBoard> completedShipBoards;
    private Hourglass hourglass;
    private final Map<ShipBoard, Integer> shipToForecasts;
    private final Set<Integer> blockedForecasts;

    public ShipBuildingState() {
        this.completedShipBoards = new HashSet<>();
        this.shipToForecasts = new HashMap<>();
        this.blockedForecasts = new HashSet<>();
        this.componentBank = new ComponentBank();
        try {
            componentBank.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        this.hourglass = game.getGameFactory().createHourglass();
    }

    @Override
    public void requestRandComponent(ShipBoard shipBoard) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.offerComponent(componentBank.drawRandComponent());
    }

    @Override
    public void requestComponent(ShipBoard shipBoard, int componentId) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.offerComponent(componentBank.removeUncoveredComponent(componentId));  //TODO: define componentIdentifiers
    }

    @Override
    public void rejectComponent(ShipBoard shipBoard) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        componentBank.addToUncoveredComponents(shipBoard.rejectComponent());
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
        shipBoard.placeComponent(point, orientation);
    }

    @Override
    public void flipHourglass(ShipBoard shipBoard) {
        if (hourglass.isLastFlip() && !completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship must be completed before the last flip");
        }
        hourglass.flip(() -> System.err.println("Hourglass is done"), this::endBuilding);
    }

    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        releaseForecast(shipBoard);
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

    private void endBuilding() {
        game.setCurrentState(new ShipCorrectionState());
    }

    @VisibleForTesting
    public ComponentBank getComponentBank() {
        return componentBank;
    }

    @VisibleForTesting
    public Set<ShipBoard> getCompletedShipBoards() {
        return new HashSet<>(completedShipBoards);
    }

    @VisibleForTesting
    public Hourglass getHourglass() {
        return hourglass;
    }

    @VisibleForTesting
    public Map<ShipBoard, Integer> getShipToForecasts() {
        return new HashMap<>(shipToForecasts);
    }

    @VisibleForTesting
    public Set<Integer> getBlockedForecasts() {
        return new HashSet<>(blockedForecasts);
    }
}
