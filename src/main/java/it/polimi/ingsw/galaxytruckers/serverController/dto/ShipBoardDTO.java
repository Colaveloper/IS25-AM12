package it.polimi.ingsw.galaxytruckers.serverController.dto;

import java.awt.*;
import java.io.Serializable;
import java.util.Map;

public record ShipBoardDTO(
        Map<Point, Integer> componentMap
) implements Serializable {
}
