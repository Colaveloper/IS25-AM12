package it.polimi.ingsw.galaxytruckers.view.model.state;

 import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public final class DrawCardState extends AdventureState {

    private final boolean imLeader;
    private boolean hasDrawn = false;

    public DrawCardState(boolean imLeader) {
        this.imLeader = imLeader;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        List<StateActions> actions = new ArrayList<>();
        if(imLeader) {
            if (hasDrawn) actions.add(StateActions.GO_NEXT);
            else actions.add(StateActions.DRAW_CARD);
        }
        actions.addAll(super.getAvailableActions());
        return actions;
    }

    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {
        game.setCurrentAdventureCard(adventureCard);
        hasDrawn = true;
        game.getObservers().forEach(observer -> observer.notifyDrawCard(adventureCard));
    }

    public boolean getImLeader() {
        return imLeader;
    }

}
