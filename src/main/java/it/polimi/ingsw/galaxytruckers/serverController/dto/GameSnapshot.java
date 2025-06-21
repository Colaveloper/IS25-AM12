package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.serverController.dto.states.ComplexStateDTO;

import java.io.Serializable;
import java.util.Map;

public record GameSnapshot(
        FlightBoardDTO flightBoardDTO,
        ComplexStateDTO state,
        Map<String, ShipBoardDTO> ships,
        int currentCardId
) implements Serializable {
}
