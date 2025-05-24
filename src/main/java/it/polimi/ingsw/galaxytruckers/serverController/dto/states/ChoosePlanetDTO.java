package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.util.List;

public record ChoosePlanetDTO(String playerName, List<Integer> availablePlanets) implements StateDTO {
}
