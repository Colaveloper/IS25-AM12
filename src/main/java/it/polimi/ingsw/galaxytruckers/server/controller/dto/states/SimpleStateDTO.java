package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

/**
 * DTO for a simple state in the Galaxy Truckers game.
 * This record contains the player's name and the type of state.
 *
 * @param playerName the name of the player associated with this state
 * @param type       the type of state represented by this DTO
 */
public record SimpleStateDTO(
        String playerName,
        StateDTOType type
) implements StateDTO, ComplexStateDTO{
}
