package it.polimi.ingsw.galaxytruckers.view.model.state;

/**
 * Interface representing the state of an adventure in the game.
 * This interface is used to define various states that can occur during an adventure.
 * It is implemented by several classes that represent specific states of the adventure.
 */
public sealed interface AdventureStateInterface permits
        AdventureState,
        ActivateState,
        AddGoodsState,
        ChoosePlanetState,
        ChooseShipPieceState,
        DrawCardState,
        GrabRewardState,
        RemoveCrewState,
        RemoveGoodsState {
}
