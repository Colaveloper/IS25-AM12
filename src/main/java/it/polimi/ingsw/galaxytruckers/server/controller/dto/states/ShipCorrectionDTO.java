package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DTO for the ShipCorrection state.
 *
 * @param validShips     a set of names of players whose ships are valid
 * @param shipPieces     a map where keys are ship names and values are lists of sets of points representing the ship pieces
 * @param shouldDiscard  a boolean indicating whether the current ship should be discarded
 */
public record ShipCorrectionDTO(
        Set<String> validShips, Map<String, List<Set<Point>>> shipPieces,
        boolean shouldDiscard
) implements StateDTO, ComplexStateDTO {
}
