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

public final class SecondShipBuildingState extends ShipBuildingState {

    // if there is a color, then that player has taken the forecast
    private final ShipBoard[] blockedForecasts = new ShipBoard[]{null,null,null};
    private final Map<ShipBoard,Integer> shipToForecast = new HashMap<>();
    private final Hourglass hourglass = new Hourglass(3);
    private boolean forecastAcquired = false;
    private boolean hourglassEndTriggered = false;

    @Override
    public void setGame(Game game) {
        super.setGame(game);
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

    public ShipBoard[] getBlockedForecasts() {
        return blockedForecasts;
    }

    public Hourglass getHourglass() {
        return hourglass;
    }
}
