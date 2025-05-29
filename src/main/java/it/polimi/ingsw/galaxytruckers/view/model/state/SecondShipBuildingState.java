package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class SecondShipBuildingState extends ShipBuildingState {
    private static final List<StateActions> availableActions = List.of(
            StateActions.STASH_COMPONENT,
            StateActions.GRAB_STASHED_COMPONENT,
            StateActions.FLIP_HOURGLASS,
            StateActions.ACQUIRE_FORECAST
            //StateActions.RELEASE_FORECAST
    );

    private List<AdventureCard> forecastDeck;

    // if there is a color, then that player has taken the forecast
    private final ShipBoard[] blockedForecasts = new ShipBoard[]{null,null,null};
    private final Map<ShipBoard,Integer> shipToForecast = new HashMap<>();
    private final Hourglass hourglass = new Hourglass(3);

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(super.getAvailableActions());
        actions.addAll(availableActions);
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
        this.forecastDeck = adventureCards;
        game.getObservers().forEach(observer -> observer.setForecastDeck(adventureCards));
    }


    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard) {
        int index = shipToForecast.get(shipBoard);
        blockedForecasts[index] = null;
        shipToForecast.remove(shipBoard);
        game.getObservers().forEach(observer -> observer.notifyReleaseForecast(shipBoard, index));
    }

    public List<AdventureCard> getForecastDeck() {
        return forecastDeck;
    }

    public ShipBoard[] getBlockedForecasts() {
        return blockedForecasts;
    }
}
