package it.polimi.ingsw.galaxytruckers.view.model.state;

 import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public final class DrawCardState extends AdventureState {

    private final boolean imLeader;
    private boolean hasDrawn = false;

    public DrawCardState(ShipBoard myShip, ShipBoard currentShip) {
        this.currentShip = currentShip;
        this.myShip = myShip;
        this.imLeader = currentShip.equals(myShip);
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
        currentCard = adventureCard;
        hasDrawn = true;
        game.getObservers().forEach(observer -> observer.notifyDrawCard(adventureCard));
    }

    public void setHasDrawn(boolean hasDrawn) {
        this.hasDrawn = hasDrawn;
    }
}
