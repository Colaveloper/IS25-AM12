package it.polimi.ingsw.galaxytruckers.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.events.*;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public non-sealed abstract class ShipBuildingState extends GameState {
    private final ComponentBank componentBank;
    protected final Set<ShipBoard> completedShipBoards;

    public ShipBuildingState() {
        this.completedShipBoards = new HashSet<>();
        this.componentBank = new ComponentBank();
        try {
            componentBank.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestRandComponent(ShipBoard shipBoard) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        Component component = componentBank.drawRandComponent();
        shipBoard.offerComponent(component);
        game.getEventListener().notifyRequestFaceDownComponentEvent(shipBoard,component);
    }

    @Override
    public void requestComponent(ShipBoard shipBoard, int componentId) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        Component component = componentBank.removeUncoveredComponent(componentId);
        shipBoard.offerComponent(component);
        game.getEventListener().notifyRequestFaceUpComponentEvent(shipBoard,component);
    }

    @Override
    public void rejectComponent(ShipBoard shipBoard) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        Component component = shipBoard.rejectComponent();
        if (component == null) {
            throw new IllegalStateException("You don't have any component to reject");
        }
        componentBank.addToUncoveredComponents(component);
        game.getEventListener().notifyRejectComponentEvent(shipBoard);
    }

    @Override
    public void stashComponent(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("This action is not available.");
    }

    @Override
    public void grabStashedComponent(ShipBoard shipBoard, int index) {
        throw new UnsupportedOperationException("This action is not available.");
    }

    @Override
    public void placeComponent(ShipBoard shipBoard, Point point, int orientation) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.placeComponent(point, orientation);
        Component component = shipBoard.getLastComponent().orElseThrow();
        game.getEventListener().notifyPlaceComponentEvent(shipBoard,orientation, point);
    }

    @Override
    public void flipHourglass(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        int position = game.getFlightBoard().getStartingPositionsLeft().stream()
                .mapToInt(x -> x)
                .min().orElseThrow(() -> new IllegalStateException("There are no more available positions"));
        completedShipBoards.add(shipBoard);
        if (game.getFlightBoard().placeShipOnFlightBoard(shipBoard, position)) {
            endBuilding();
        }
        game.getEventListener().notifyFlightBoardUpdateEvent(shipBoard,position);
    }

    @Override
    public void acquireForecast(ShipBoard shipBoard, int deckIndex) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    @Override
    public void releaseForecast(ShipBoard shipBoard) {
        throw new UnsupportedOperationException("This action is not available at level " + game.getLevel());
    }

    protected abstract void endBuilding();

    @VisibleForTesting
    public ComponentBank getComponentBank() {
        return componentBank;
    }

    @VisibleForTesting
    public Set<ShipBoard> getCompletedShipBoards() {
        return new HashSet<>(completedShipBoards);
    }

    @VisibleForTesting
    protected ShipBuildingState(ComponentBank testBank) {
        this.completedShipBoards = new HashSet<>();
        this.componentBank = testBank;
        try {
            componentBank.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
