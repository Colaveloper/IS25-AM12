package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event signaling that a new card has been drawn.
 *
 * @param cardId the ID of the newly drawn card
 */
public record NewCardEvent(int cardId) implements LobbyEvent {
}
