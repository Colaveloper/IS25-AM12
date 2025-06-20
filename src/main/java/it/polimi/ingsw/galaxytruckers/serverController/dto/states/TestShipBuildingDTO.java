package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.serverController.dto.BuildingDataDTO;

public record TestShipBuildingDTO(
        BuildingDataDTO data
) implements ComplexStateDTO {}
