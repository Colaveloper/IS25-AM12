package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.Game;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.List;
import java.util.Set;

public sealed abstract class AdventureState extends GameState permits
        ActivateState,
        AddGoodsState,
        ChoosePlanetState,
        ChooseShipPieceState,
        DrawCardState,
        GrabRewardState,
        RemoveCrewState,
        RemoveGoodsState
{



    protected boolean imOut;
    protected ShipBoard currentShip;

    public boolean getImOut() { return imOut; }

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

    public ShipBoard getShipBoard() {
        return currentShip;
    }
}
