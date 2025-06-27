package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

import it.polimi.ingsw.galaxytruckers.server.controller.dto.BuildingDataDTO;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.HourglassDTO;

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
