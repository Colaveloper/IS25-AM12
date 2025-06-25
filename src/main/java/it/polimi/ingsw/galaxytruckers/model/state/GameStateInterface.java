package it.polimi.ingsw.galaxytruckers.model.state;

/**
 * Interface representing the game state.
 * This interface is sealed to restrict its implementation to specific classes.
 */
public sealed interface GameStateInterface permits AddGoodsState, ChoosePlanetState, ChooseShipPieceState,
                                                   DeclareEnginePowerState, DeclareFirePowerState, DrawCardState,
                                                   GameState, GrabRewardState, HandleProjectileState,
                                                   RemoveCrewState, RemoveGoodsState, SecondShipBuildingState,
                                                   ShipCorrectionState, ShipInitializationState, TestShipBuildingState {
}
