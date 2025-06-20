package it.polimi.ingsw.galaxytruckers.serverController.dto;

import java.io.Serializable;

public record HourglassDTO(int timeLeft, int flipsLeft, boolean isRunning) implements Serializable {
}
