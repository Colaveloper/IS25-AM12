package it.polimi.ingsw.galaxytruckers.server.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.server.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents the ship building state for games of level SECOND, where players
 * can build their ships and manage forecasts.
 */
public non-sealed class SecondShipBuildingState extends ShipBuildingState implements GameStateInterface {
    private final Hourglass hourglass;
    private final Map<ShipBoard, Integer> shipToForecasts = new HashMap<>();
    private final Set<Integer> blockedForecasts = new HashSet<>();

    private final Object forecastLock = new Object();

    public SecondShipBuildingState() {
        super();
        this.hourglass = new Hourglass(3);
    }

    /**
     * {@inheritDoc}
     * <p>In this state, the hourglass is started and the game is set.
     * </p>
     */
    @Override
    public void setGame(Game game) {
        super.setGame(game);
        hourglass.flip(this::notifyHourglassEnd);
    }

    /**
     * {@inheritDoc}.
     * <p>
     * The action is implemented in this state and calls
     * {@link Hourglass#flip(Runnable)} to flip the hourglass.
     * </p>
     *
     * @throws IllegalStateException if it's the last flip and the
     *                               ship board is not completed.
     */
    @Override
    public void flipHourglass(ShipBoard shipBoard) {
        synchronized (this.hourglass) {
            boolean isLast = hourglass.isLastFlip();
            if (isLast) {
                if (!getCompletedShipBoards().contains(shipBoard)) {
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
    }

    /**
     * {@inheritDoc}.
     * <p>The action is implemented for this state</p>
     *
     * @throws IllegalStateException if the ship board is already completed.
     */
    @Override
    public void stashComponent(ShipBoard shipBoard) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.stashComponent();
    }

    /**
     * {@inheritDoc}
     * <p>The action is implemented for this state</p>
     *
     * @throws IllegalStateException if the ship board is already completed.
     */
    @Override
    public void grabStashedComponent(ShipBoard shipBoard, int index) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        shipBoard.grabStashedComponent(index);
    }

    /**
     * {@inheritDoc}
     * <p>The action is implemented for this state</p>
     *
     * @throws IllegalStateException if the ship board is already completed.
     */
    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        releaseForecast(shipBoard);
        game.getFlightBoard().placeShipOnFlightBoard(shipBoard, startingPosition);
        completeShipBoard(shipBoard);
    }

    /**
     * {@inheritDoc}
     * <p>The action is implemented for this state</p>
     *
     * @throws IllegalStateException    if the ship board is already completed
     * @throws IllegalArgumentException if the forecast is blocked by another player
     */
    @Override
    public void acquireForecast(ShipBoard shipBoard, int deckIndex) {
        if (getCompletedShipBoards().contains(shipBoard)) {
            throw new IllegalStateException("Ship Board already completed");
        }
        synchronized (forecastLock) {
            if (blockedForecasts.contains(deckIndex)) {
                throw new IllegalArgumentException("Deck is unavailable");
            }
            blockedForecasts.add(deckIndex);
            shipToForecasts.put(shipBoard, deckIndex);
            game.getEventListener().notifyPeekForecastEvent(shipBoard, deckIndex);
            game.getEventListener().notifyForecastDetailsEvent(shipBoard, game.getDeck().getForecastDeck(deckIndex));
        }
    }

    /**
     * {@inheritDoc}
     * <p>The action is implemented for this state</p>
     */
    @Override
    public void releaseForecast(ShipBoard shipBoard) {
        synchronized (forecastLock) {
            if (shipToForecasts.containsKey(shipBoard)) {
                int index = shipToForecasts.remove(shipBoard);
                blockedForecasts.remove(index);
                game.getEventListener().notifyReleaseForecastEvent(shipBoard, index);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>In this state, the hourglass is stopped, forecasts are released and
     * ships are placed on the flight board</p>
     */
    @Override
    protected void endBuilding() {
        synchronized (endLock) {
            if (!expired) {
                expired = true;
                game.submitStateTransition(() -> {
                    hourglass.stop();
                    Set<ShipBoard> unfinishedShipBoards = new HashSet<>(game.getShipBoards());
                    unfinishedShipBoards.removeAll(completedShipBoards);
                    for (ShipBoard shipBoard : unfinishedShipBoards) {
                        releaseForecast(shipBoard);
                        placeShipOnFlightBoard(shipBoard);
                    }
                    game.getShipBoards().forEach(ShipBoard::finishBuilding);
                    game.setCurrentState(game.getGameFactory().createShipCorrectionState());
                });
            }
        }
    }

    /**
     * Notifies the end of the hourglass event to the event listener.
     */
    protected void notifyHourglassEnd() {
        game.getEventListener().notifyHourglassEndEvent();
    }

    /**
     * Returns the hourglass used in this state.
     *
     * @return the hourglass
     */
    @VisibleForTesting
    public Hourglass getHourglass() {
        return hourglass;
    }

    /**
     * @return a map where keys are ship boards and values are forecast indices
     * they have acquired.
     */
    @VisibleForTesting
    public Map<ShipBoard, Integer> getShipToForecasts() {
        return shipToForecasts;
    }

    /**
     * @return a set of blocked forecast indices
     */
    @VisibleForTesting
    public Set<Integer> getBlockedForecasts() {
        return blockedForecasts;
    }
}
