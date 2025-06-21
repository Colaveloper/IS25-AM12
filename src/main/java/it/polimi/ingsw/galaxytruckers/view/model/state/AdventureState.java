package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;

public sealed abstract class AdventureState extends GameState permits
                                                              ActivateState,
                                                              AddGoodsState,
                                                              ChoosePlanetState,
                                                              ChooseShipPieceState,
                                                              DrawCardState,
                                                              GrabRewardState,
                                                              RemoveCrewState,
                                                              RemoveGoodsState {


    protected boolean imOut;
    protected ShipBoard currentShip;
    protected AdventureCard currentCard;

    public boolean getImOut() {
        return imOut;
    }

    public AdventureCard getCurrentCard() {
        return currentCard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        if (!imOut) return List.of(StateActions.GIVE_UP);
        else return List.of();
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        imOut = game.getGivenUpShips().contains(myShip);
    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        shipBoard.loseCrew(point);
        game.getObservers().forEach(observer -> observer.notifyLoseCrew(shipBoard, point));
    }

    public ShipBoard getShipBoard() {
        return currentShip;
    }
}
