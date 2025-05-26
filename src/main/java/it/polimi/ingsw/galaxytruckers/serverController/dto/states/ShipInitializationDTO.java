package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;
import java.util.List;
import java.util.Map;

public record ShipInitializationDTO(Map<Player, Map<CrewType, List<Point>>> crewTypeToCabins) implements StateDTO{
}
