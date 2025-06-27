package it.polimi.ingsw.galaxytruckers.server.controller.dto;

import java.io.Serializable;

/**
 * DTO for the Hourglass component.
 *
 * @param timeLeft   the time left in seconds
 * @param flipsLeft  the number of flips left
 * @param isRunning  whether the hourglass is currently running
 */
public record HourglassDTO(int timeLeft, int flipsLeft, boolean isRunning) implements Serializable {
}
