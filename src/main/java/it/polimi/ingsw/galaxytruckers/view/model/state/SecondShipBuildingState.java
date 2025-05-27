package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.*;

public final class SecondShipBuildingState extends ShipBuildingState {
    private static final List<StateActions> availableActions = List.of(
            StateActions.STASH_COMPONENT,
            StateActions.GRAB_STASHED_COMPONENT,
            StateActions.FLIP_HOURGLASS,
            StateActions.ACQUIRE_FORECAST,
            StateActions.RELEASE_FORECAST);

    private List<AdventureCard> forecastDeck;
    private final ShipBoard[] blockedForecasts = new ShipBoard[]{null, null, null};
    private Hourglass hourglass = new Hourglass(3);

    public SecondShipBuildingState() {
        super();
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>(super.getAvailableActions());
        actions.addAll(availableActions);
        //TODO: implement conditional available action if needed
        return actions;
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard) {
        shipBoard.stashComponent();
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        shipBoard.grabStashedComponent(index);
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        hourglass.flip();
    }

    @Override
    public void notifyHourglassEnd() {
        hourglass.end();
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        blockedForecasts[deckIndex] = shipBoard;
        shipBoard.weldLastComponent();
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        this.forecastDeck = adventureCards;
    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard) {
        for (int i = 0; i < this.blockedForecasts.length; i++) {
            if (blockedForecasts[i].equals(shipBoard)) {
                blockedForecasts[i] = null;
                return;
            }
        }
    }

    public ShipBoard[] getLockedForecasts() {
        return blockedForecasts;
    }
}
