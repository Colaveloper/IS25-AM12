package it.polimi.ingsw.galaxytruckers.serverController.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * DTO for building data in the game.
 *
 * @param numCovered     the number of covered components in the component bank
 * @param uncoveredIds   a list of IDs of uncovered components in the component bank
 * @param completedNames a set of names of players who have completed their ships
 */
public record BuildingDataDTO(
        int numCovered,
        List<Integer> uncoveredIds,
        Set<String> completedNames) implements Serializable {
}
