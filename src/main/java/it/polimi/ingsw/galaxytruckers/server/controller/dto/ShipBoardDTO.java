package it.polimi.ingsw.galaxytruckers.server.controller.dto;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for the ship board in the Galaxy Truckers game.
 *
 * @param componentMap a map of points to ComponentDTOs representing the components on the ship board
 * @param lastComponent the id of the component in hand, or -1 if no component is in hand
 * @param lastPosition the position of the component in hand, or null if there is no placed component in hand
 * @param stashedComponents a list of component ids that are stashed
 * @param credits the number of credits the player has
 * @param losses the number of losses the player has incurred
 */
public record ShipBoardDTO(
        Map<Point, ComponentDTO> componentMap,
        int lastComponent,
        Point lastPosition,
        List<Integer> stashedComponents,
        int credits,
        int losses
) implements Serializable {
}
