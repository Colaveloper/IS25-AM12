package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ShipCorrectionDTO(Set<String> validShips, Map<String, List<Set<Point>>> shipPieces, boolean shouldDiscard) implements
                                                                                                    StateDTO {
}
