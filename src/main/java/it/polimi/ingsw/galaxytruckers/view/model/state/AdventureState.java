package it.polimi.ingsw.galaxytruckers.view.model.state;

import java.util.List;

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
