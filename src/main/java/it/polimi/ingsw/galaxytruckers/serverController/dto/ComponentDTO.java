package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.serverController.dto.components.ComponentPayload;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.io.Serializable;

public record ComponentDTO(
        int id,
        Direction orientation,
        ComponentPayload payload
) implements Serializable {
}
