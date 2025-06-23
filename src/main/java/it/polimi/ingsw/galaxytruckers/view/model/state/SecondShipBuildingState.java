package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Represents the second ship building phase in the Galaxy Truckers game.
 * This state handles the building of ships for level II flights,
 * including specialized mechanics like forecast peeking and time constraints.
 * Players have a limited time governed by the hourglass to complete their ships.
 */
public final class SecondShipBuildingState extends ShipBuildingState {

    /** Array tracking which ship boards have acquired which forecast positions (null if position is available) */
    private final ShipBoard[] blockedForecasts = new ShipBoard[]{null,null,null};

    /** Map associating ship boards with their chosen forecast indices */
    private final Map<ShipBoard,Integer> shipToForecast = new HashMap<>();

    /** The hourglass managing the time constraints for this building phase */
    private final Hourglass hourglass = new Hourglass(3);

    /** Flag indicating whether the local player has acquired a forecast */
    private boolean forecastAcquired = false;

    /**
     * Creates a new SecondShipBuildingState.
     * Initializes the state and immediately starts the hourglass timer.
     */
    public SecondShipBuildingState() {
        super();
        hourglass.flip();
    }

    @Override
    public void leave() {
        super.leave();
        hourglass.end();
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(hasFinished) {
            // only allow flipping the hourglass if it's not currently running
            if (!hourglass.getIsRunning()) {
                // If this is the last flip, only allow players with ships on the flightboard to flip it
                if (hourglass.getFlipsLeft() == 1) {
                    boolean shipOnFlightboard = game.getFlightBoard().getShipToPlace().containsKey(myShip);
                    if (shipOnFlightboard) {
                        actions.add(StateActions.FLIP_HOURGLASS);
                    }
                } else {
                    actions.add(StateActions.FLIP_HOURGLASS);
                }
            }
            return actions;
        }
        if(forecastAcquired) actions.add(StateActions.RELEASE_FORECAST);
        else {
            // only allow flipping the hourglass if it's not currently running
            if (!hourglass.getIsRunning()) {
                // If this is the last flip, only allow players with ships on the flightboard to flip it
                if (hourglass.getFlipsLeft() == 1) {
                    boolean shipOnFlightboard = game.getFlightBoard().getShipToPlace().containsKey(myShip);
                    if (shipOnFlightboard) {
                        actions.add(StateActions.FLIP_HOURGLASS);
                    }
                } else {
                    actions.add(StateActions.FLIP_HOURGLASS);
                }
            }
            if(!(myShip.getLastComponent() == null) && myShip.getStashedComponents().size() < 2) actions.add(StateActions.STASH_COMPONENT);
            if(!componentInHand() && !myShip.getStashedComponents().isEmpty()) actions.add(StateActions.GRAB_STASHED_COMPONENT);
            if(!componentInHand()) actions.add(StateActions.ACQUIRE_FORECAST);
            actions.addAll(super.getAvailableActions());
            if(!hasFinished) actions.add(StateActions.PLACE_SHIP_ON_FLIGHTBOARD);
        }
        //TODO: implement conditional available action if needed
        return actions;
    }


    @Override
    public void notifyStashComponent(ShipBoard shipBoard) {
        Point prevPos = shipBoard.getLastPosition();
        Component component = shipBoard.getLastComponent();
        shipBoard.stashComponent();
        if (prevPos != null) {
            game.getObservers().forEach(observer -> observer.notifyStashComponent(shipBoard,component,prevPos));
        } else {
            game.getObservers().forEach(observer -> observer.notifyStashComponent(shipBoard,component));
        }
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        hasStashed = true;
        shipBoard.grabStashedComponent(index);
        Component component = shipBoard.getLastComponent();
        game.getObservers().forEach(observer -> observer.notifyGrabStashedComponent(shipBoard, index, component));
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        hourglass.flip();
        game.getObservers().forEach(observer -> observer.notifyFlipHourglass(shipBoard));
    }

    @Override
    public void notifyHourglassEnd() {
        hourglass.end();

        System.out.println("TIME'S UP! The hourglass has run out!");

        for (ShipBoard shipBoard : game.getShipBoards()) {
            // weld all components that have been placed
//            if (shipBoard.getLastComponent() != null && shipBoard.getLastPosition() != null) {
//                shipBoard.weldLastComponent();
//            }

            // count losses for stashed components
            if (shipBoard.getStashedComponents() != null && !shipBoard.getStashedComponents().isEmpty()) {
                int lossesFromStash = shipBoard.getStashedComponents().size();
                shipBoard.incrementLosses(lossesFromStash);
            }
        }

        // players can only place on the flight board after the hourglass ends
        game.getObservers().forEach(ModelObserver::notifyHourglassEnd);
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        blockedForecasts[deckIndex] = shipBoard;
        shipToForecast.put(shipBoard,deckIndex);
        shipBoard.weldLastComponent();
        game.getObservers().forEach(observer -> observer.notifyPeekForecast(shipBoard, deckIndex));
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        forecastAcquired = true;
        game.getObservers().forEach(observer -> observer.setForecastDeck(adventureCards));
    }

    /**
     * Checks whether the local player has acquired a forecast.
     * This is used to determine whether the player can see forecast cards
     * and potentially affects available actions.
     *
     * @return true if the local player has acquired a forecast, false otherwise
     */
    public boolean getHasForecastDeck() {
        return forecastAcquired;
    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, boolean isMyShip) {
        if(isMyShip) forecastAcquired = false;
        int index = shipToForecast.get(shipBoard);
        blockedForecasts[index] = null;
        shipToForecast.remove(shipBoard);
        game.getObservers().forEach(observer -> observer.notifyReleaseForecast(shipBoard, index));
    }

    /**
     * Gets the array tracking which ship boards have acquired which forecast positions.
     * A null value at an index indicates that forecast position is available.
     *
     * @return The array of ship boards corresponding to blocked forecast positions
     */
    public ShipBoard[] getBlockedForecasts() {
        return blockedForecasts;
    }

    /**
     * Gets the hourglass that manages time constraints for this building phase.
     * This can be used to check remaining time, flips, and running status.
     *
     * @return The hourglass instance for this state
     */
    public Hourglass getHourglass() {
        return hourglass;
    }
}
