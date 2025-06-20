package it.polimi.ingsw.galaxytruckers.serverController.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public record BuildingDataDTO(
        int numCovered,
        List<Integer> uncoveredIds,
        Set<String> completedNames) implements Serializable {
}
