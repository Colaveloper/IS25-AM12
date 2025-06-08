package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.io.Serializable;

public sealed interface StateDTO extends Serializable permits AddGoodsDTO, ChooseShipPieceDTO,
                                                              HandleProjectileDTO, RemoveCrewDTO, RemoveGoodsDTO,
                                                              ShipBuildingDTO, ShipCorrectionDTO, ShipInitializationDTO,
                                                              SimpleStateDTO {
}
