package it.polimi.ingsw.galaxytruckers.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SecondShipBuildingState extends ShipBuildingState {
    private final Hourglass hourglass = new Hourglass(3);
    private final Map<ShipBoard, Integer> shipToForecasts = new HashMap<>();
    private final Set<Integer> blockedForecasts = new HashSet<>();

    public SecondShipBuildingState() {
        super();
    }

    @Override
    public void flipHourglass(ShipBoard shipBoard) {
        if (hourglass.isLastFlip()) {
            if (!completedShipBoards.contains(shipBoard)) {
                throw new IllegalStateException("Ship must be completed before the last flip");
            }
            hourglass.flip(this::endBuilding);
        } else {
            hourglass.flip(this::notifyHourglassEnd);
        }
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

    @Override
    protected void endBuilding() {
        Set<ShipBoard> unfinishedShipBoards = new HashSet<>(game.getShipBoards());
        unfinishedShipBoards.removeAll(completedShipBoards);
        for (ShipBoard shipBoard : unfinishedShipBoards) {
            releaseForecast(shipBoard);
            shipBoard.finishBuilding();
        }
        game.setCurrentState(new ShipCorrectionState()); //TODO: set SecondShipCorrectionState
    }

    protected void notifyHourglassEnd() {
        System.out.println("Hourglass has finished");
        //TODO : implement notify with events
    }

    @VisibleForTesting
    public Hourglass getHourglass() {
        return hourglass;
    }

    @VisibleForTesting
    public Map<ShipBoard, Integer> getShipToForecasts() {
        return shipToForecasts;
    }

    @VisibleForTesting
    public Set<Integer> getBlockedForecasts() {
        return blockedForecasts;
    }
}
