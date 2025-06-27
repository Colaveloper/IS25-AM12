package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

import java.io.Serializable;

/**
 * Represents the state of the game in a DTO format.
 * This interface is used to define various states that can be sent to clients.
 * It is sealed to restrict its implementations to specific classes.
 */
public sealed interface StateDTO extends Serializable permits AddGoodsDTO, ChoosePlanetDTO, ChooseShipPieceDTO,
                                                              HandleProjectileDTO, RemoveCrewDTO, RemoveGoodsDTO,
                                                              ShipBuildingDTO, ShipCorrectionDTO, ShipInitializationDTO,
                                                              SimpleStateDTO {
}
