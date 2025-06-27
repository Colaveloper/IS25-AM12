package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;

import java.awt.*;
import java.util.Map;
import java.util.Set;

/**
 * DTO for the Ship Initialization state.
 *
 * @param crewTypeToCabins a map where the key is the ship name and the value is another map
 *                         that maps crew types to sets of cabin points where that crew type
 *                         can be placed.
 */
public record ShipInitializationDTO(
        Map<String, Map<CrewType, Set<Point>>> crewTypeToCabins
) implements StateDTO, ComplexStateDTO {
}
