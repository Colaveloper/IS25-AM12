package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.io.Serializable;

public sealed interface ComplexStateDTO extends Serializable permits SecondShipBuildingDTO,
                                                                     TestShipBuildingDTO, ShipInitializationDTO {
}
