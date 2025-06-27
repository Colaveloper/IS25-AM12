package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

import it.polimi.ingsw.galaxytruckers.server.controller.dto.BuildingDataDTO;

/**
 * DTO for the TestShipBuilding state.
 *
 * @param data ship building phase data
 */
public record TestShipBuildingDTO(
        BuildingDataDTO data
) implements ComplexStateDTO {}
