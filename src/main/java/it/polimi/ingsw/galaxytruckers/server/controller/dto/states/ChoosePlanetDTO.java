package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

/**
 * DTO for the ChoosePlanet state.
 *
 * @param playerName the name of the player who is choosing planets
 * @param numPlanets the number of planets to choose
 */
public record ChoosePlanetDTO(String playerName, int numPlanets) implements StateDTO {
}
