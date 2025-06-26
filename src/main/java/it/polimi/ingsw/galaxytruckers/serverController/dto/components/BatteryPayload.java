package it.polimi.ingsw.galaxytruckers.serverController.dto.components;

/**
 * Represents the payload for a battery component in the Galaxy Truckers game.
 * This record contains the number of batteries available in the component.
 *
 * @param numBatteries the number of batteries in the component
 */
public record BatteryPayload(int numBatteries) implements ComponentPayload {
}
