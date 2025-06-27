package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

/**
 * DTO for the ComplexChoosePlanet state.
 * This DTO extends ChoosePlanetDTO to include additional choices available for the player.
 *
 * @param baseData the base data for choosing a planet, including player name and number of planets
 * @param choices  the array of choices already performed by other players
 */
public record ComplexChoosePlanetDTO(
        ChoosePlanetDTO baseData,
        String[] choices
) implements ComplexStateDTO {
}
