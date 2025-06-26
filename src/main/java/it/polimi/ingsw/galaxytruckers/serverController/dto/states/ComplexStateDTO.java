package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.io.Serializable;

/**
 * Marker interface for complex state DTOs that require additional information
 * beyond the basic state, such as player choices or game-specific data. Used
 * for client reconnections to send state internal information.
 */
public sealed interface ComplexStateDTO extends Serializable permits AddGoodsDTO, ChooseShipPieceDTO,
                                                                     ComplexChoosePlanetDTO, ComplexDrawCardDTO,
                                                                     HandleProjectileDTO, RemoveCrewDTO, RemoveGoodsDTO,
                                                                     SecondShipBuildingDTO, ShipCorrectionDTO,
                                                                     ShipInitializationDTO, SimpleStateDTO,
                                                                     TestShipBuildingDTO {
}
