package it.polimi.ingsw.galaxytruckers.view.model.state;

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


    @Override
    public List<StateActions> getAvailableActions() {
        return List.of(StateActions.GIVE_UP);
    }
}
