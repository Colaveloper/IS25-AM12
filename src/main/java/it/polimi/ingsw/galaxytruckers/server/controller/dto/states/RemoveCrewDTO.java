package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

/**
 * DTO for RemoveCrewState.
 *
 * @param playerName the name of the player who is removing crew
 * @param crewLoss   the number of crew members to be removed
 */
public record RemoveCrewDTO(
        String playerName,
        int crewLoss
) implements StateDTO, ComplexStateDTO {
}
