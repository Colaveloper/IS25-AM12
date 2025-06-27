package it.polimi.ingsw.galaxytruckers.server.controller.dto;

import it.polimi.ingsw.galaxytruckers.server.controller.dto.states.ComplexStateDTO;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO representing a snapshot of the general game state.
 *
 * @param flightBoardDTO the flight board data transfer object
 * @param state the complex state data transfer object
 * @param ships a map of player names to their corresponding ship board data transfer objects
 * @param currentCardId the ID of the current card being played, or -1 if no card is being played
 */
public record GameSnapshot(
        FlightBoardDTO flightBoardDTO,
        ComplexStateDTO state,
        Map<String, ShipBoardDTO> ships,
        int currentCardId
) implements Serializable {
}
