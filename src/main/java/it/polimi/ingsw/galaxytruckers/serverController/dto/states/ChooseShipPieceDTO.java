package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.awt.*;
import java.util.List;
import java.util.Set;

public record ChooseShipPieceDTO(String playerName, List<Set<Point>> shipPieces) implements StateDTO {
}
