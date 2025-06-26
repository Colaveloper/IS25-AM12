package it.polimi.ingsw.galaxytruckers.serverController.dto.components;

/**
 * Represents the payload for an activatable component in the Galaxy Truckers game.
 * This record contains a boolean indicating whether the component is active.
 *
 * @param active true if the component is active, false otherwise
 */
public record ActivatablePayload(boolean active) implements ComponentPayload {
}
