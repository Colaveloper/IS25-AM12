package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.serverController.dto.BuildingDataDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.HourglassDTO;

import java.util.Map;

public record SecondShipBuildingDTO(
        BuildingDataDTO baseData,
        HourglassDTO hourglass,
        Map<String, Integer> blockedForecasts
) implements ComplexStateDTO {}
