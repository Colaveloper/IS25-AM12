package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.serverController.dto.BuildingDataDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.HourglassDTO;

import java.util.Map;

/**
 * DTO for the SecondShipBuilding state.
 *
 * @param baseData       the base data of the building phase
 * @param hourglass      information about the hourglass for this phase
 * @param blockedForecasts a map of forecasts that are blocked during this phase
 */
public record SecondShipBuildingDTO(
        BuildingDataDTO baseData,
        HourglassDTO hourglass,
        Map<String, Integer> blockedForecasts
) implements ComplexStateDTO {}
