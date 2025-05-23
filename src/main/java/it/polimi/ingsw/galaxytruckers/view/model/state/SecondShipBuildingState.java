package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;

import java.util.*;

public class SecondShipBuildingState extends ShipBuildingState {
    private static final List<StateActions> availableActions = List.of(
            StateActions.STASH_COMPONENT,
            StateActions.GRAB_STASHED_COMPONENT,
            StateActions.FLIP_HOURGLASS,
            StateActions.ACQUIRE_FORECAST,
            StateActions.RELEASE_FORECAST);
    private List<AdventureCard> forecastDeck;
    private final Set<Integer> blockedForecasts = new HashSet<>();

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
}
