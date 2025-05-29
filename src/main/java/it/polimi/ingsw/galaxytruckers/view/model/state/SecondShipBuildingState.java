package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;
import it.polimi.ingsw.galaxytruckers.view.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.*;

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
    private final ObservableList<GameColor> blockedForecasts = new ObservableList<>();
    private final Hourglass hourglass = new Hourglass(3);

    public SecondShipBuildingState() {
        super();
        blockedForecasts.add(null);
        blockedForecasts.add(null);
        blockedForecasts.add(null);
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
        System.out.println("Hourglass end");
        hourglass.end();
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        blockedForecasts.set(deckIndex, shipBoard.getColor());
        shipBoard.weldLastComponent();
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        this.forecastDeck = adventureCards;
    }

    public List<AdventureCard> getForecastDeck() {
        return forecastDeck;
    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard) {
        for (int i = 0; i < this.blockedForecasts.size(); i++) {
            if (blockedForecasts.get(i).equals(shipBoard.getColor())) {
                blockedForecasts.set(i, null);
                return;
            }
        }
    }

    public ObservableList<GameColor> getLockedForecastsProperty() {
        return blockedForecasts;
    }
}
