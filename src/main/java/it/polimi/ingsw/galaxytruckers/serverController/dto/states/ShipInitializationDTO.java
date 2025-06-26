package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;
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
