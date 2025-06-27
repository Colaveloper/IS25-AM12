package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

/**
 * DTO for ComplexDrawCardState.
 * This DTO is used to indicate whether a player has drawn a card or not.
 *
 * @param playerName the name of the player who is drawing a card
 * @param hasDrawn   indicates if the player has drawn a card
 */
public record ComplexDrawCardDTO(String playerName, boolean hasDrawn) implements ComplexStateDTO {
}
