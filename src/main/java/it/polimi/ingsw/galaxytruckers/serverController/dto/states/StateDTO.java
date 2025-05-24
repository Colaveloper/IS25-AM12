package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.io.Serializable;

public sealed interface StateDTO extends Serializable permits AddGoodsDTO, ChoosePlanetDTO, ChooseShipPieceDTO,
                                                              HandleProjectileDTO, RemoveCrewDTO, RemoveGoodsDTO,
                                                              ShipCorrectionDTO, ShipInitializationDTO, SimpleStateDTO {
}
