package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ShipInitializationDTO(Map<String, Map<CrewType, Set<Point>>> crewTypeToCabins) implements StateDTO{
}
