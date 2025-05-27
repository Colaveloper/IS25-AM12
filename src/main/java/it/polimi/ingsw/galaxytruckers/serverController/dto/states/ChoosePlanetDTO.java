package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.util.List;
import java.util.Set;

public record ChoosePlanetDTO(String playerName, Set<Integer> availablePlanets) implements StateDTO {
}
