package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * DTO for the ChooseShipPiece state.
 *
 * @param playerName the name of the player who is choosing ship pieces
 * @param shipPieces the list of sets of points representing the ship pieces available for selection
 */
public record ChooseShipPieceDTO(String playerName, List<Set<Point>> shipPieces) implements StateDTO, ComplexStateDTO {
}
