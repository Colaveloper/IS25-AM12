package it.polimi.ingsw.galaxytruckers.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SecondShipBuildingState extends ShipBuildingState {
    private final Hourglass hourglass;
    private final Map<ShipBoard, Integer> shipToForecasts = new HashMap<>();
        private final Set<Integer> blockedForecasts = new HashSet<>();

    public SecondShipBuildingState() {
        super();
        this.hourglass = new Hourglass(3);
    }

    @Override
    public void flipHourglass(ShipBoard shipBoard) {
        boolean isLast = hourglass.isLastFlip();
        if (isLast) {
            if (!completedShipBoards.contains(shipBoard)) {
                throw new IllegalStateException("Ship must be completed before the last flip");
            }
            hourglass.flip(() -> {
                notifyHourglassEnd();
                endBuilding();
            });
        } else {
            hourglass.flip(this::notifyHourglassEnd);
        }
        game.getEventListener().notifyFlipHourglassEvent(shipBoard);
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        hourglass.setDuration(3); //TODO: remove this line
        game.getEventListener().notifyGameStateUpdateEvent(this);
        game.getEventListener().notifyFlipHourglassEvent(game.getShipBoards().stream().findAny().orElseThrow());
        hourglass.flip(this::notifyHourglassEnd);
    }

    @Override
    public void stashComponent(ShipBoard shipBoard) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.stashComponent();
        game.getEventListener().notifyStashComponentEvent(shipBoard);
    }

    @Override
    public void grabStashedComponent(ShipBoard shipBoard, int index) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.grabStashedComponent(index);
        game.getEventListener().notifyGrabStashedComponentEvent(shipBoard, index);
    }

    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        releaseForecast(shipBoard);
        game.getFlightBoard().placeShipOnFlightBoard(shipBoard, startingPosition);
        completedShipBoards.add(shipBoard);
        game.getEventListener().notifyFlightBoardUpdateEvent(shipBoard, startingPosition);
    }

    @Override
    public void acquireForecast(ShipBoard shipBoard, int deckIndex) {
        if (completedShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        if (blockedForecasts.contains(deckIndex)) {
            throw new IllegalArgumentException("Deck is unavailable");
        }
        blockedForecasts.add(deckIndex);
        shipToForecasts.put(shipBoard, deckIndex);
        game.getEventListener().notifyPeekForecastEvent(shipBoard,deckIndex);
        game.getEventListener().notifyForecastDetailsEvent(shipBoard,game.getDeck().getForecastDeck(deckIndex));
    }

    @Override
    public void releaseForecast(ShipBoard shipBoard) {
        if (shipToForecasts.containsKey(shipBoard)) {
            int index = shipToForecasts.remove(shipBoard);
            blockedForecasts.remove(index);
            game.getEventListener().notifyReleaseForecastEvent(shipBoard,index);
        }
    }

    @Override
    protected void endBuilding() {
        synchronized (game.getLock()) {
            Set<ShipBoard> unfinishedShipBoards = new HashSet<>(game.getShipBoards());
            unfinishedShipBoards.removeAll(completedShipBoards);
            for (ShipBoard shipBoard : unfinishedShipBoards) {
                releaseForecast(shipBoard);
                shipBoard.finishBuilding();
                placeShipOnFlightBoard(shipBoard);
            }
            game.setCurrentState(new SecondShipCorrectionState());
        }
    }

    protected void notifyHourglassEnd() {
        System.out.println("Hourglass has finished");
        game.getEventListener().notifyHourglassEndEvent();
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
