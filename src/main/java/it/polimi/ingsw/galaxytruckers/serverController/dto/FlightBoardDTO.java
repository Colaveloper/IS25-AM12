package it.polimi.ingsw.galaxytruckers.serverController.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for the flight board, which maps player names to their respective positions on the board.
 * @param playerToPlace a map where keys are player names and values are their positions on the flight board
 */
public record FlightBoardDTO(
        Map<String, Integer> playerToPlace
) implements Serializable {
}
