package it.polimi.ingsw.galaxytruckers.model.state;

public sealed interface GameStateInterface permits AddGoodsState, ChoosePlanetState, ChooseShipPieceState,
                                                   DeclareEnginePowerState, DeclareFirePowerState, DrawCardState,
                                                   EndGameState, GameState, GrabRewardState, HandleProjectileState,
                                                   RemoveCrewState, RemoveGoodsState, SecondShipBuildingState,
                                                   ShipCorrectionState, ShipInitializationState, TestShipBuildingState {
}
