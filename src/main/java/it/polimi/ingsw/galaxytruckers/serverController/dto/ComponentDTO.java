package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.serverController.dto.components.ComponentPayload;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.io.Serializable;

/**
 * DTO representing a component in the Galaxy Truckers game.
 *
 * @param id the unique identifier of the component
 * @param orientation the orientation of the component in the ship
 * @param payload the payload of the component, which can vary based on the type of component
 */
public record ComponentDTO(
        int id,
        Direction orientation,
        ComponentPayload payload
) implements Serializable {
}
