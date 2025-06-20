package it.polimi.ingsw.galaxytruckers.serverController.dto;

import java.io.Serializable;
import java.util.Map;

public record FlightBoardDTO(
        Map<String, Integer> playerToPlace
) implements Serializable {
}
