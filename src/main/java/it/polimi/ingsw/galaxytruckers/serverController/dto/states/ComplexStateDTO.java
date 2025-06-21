package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.io.Serializable;

public sealed interface ComplexStateDTO extends Serializable permits AddGoodsDTO, ChooseShipPieceDTO,
                                                                     ComplexChoosePlanetDTO, ComplexDrawCardDTO,
                                                                     HandleProjectileDTO, RemoveCrewDTO, RemoveGoodsDTO,
                                                                     SecondShipBuildingDTO, ShipCorrectionDTO,
                                                                     ShipInitializationDTO, SimpleStateDTO,
                                                                     TestShipBuildingDTO {
}
