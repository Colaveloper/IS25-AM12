package it.polimi.ingsw.galaxytruckers.serverController.dto;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record ShipBoardDTO(
        Map<Point, ComponentDTO> componentMap,
        int lastComponent,
        Point lastPosition,
        List<Integer> stashedComponents,
        int credits,
        int losses
) implements Serializable {
}
