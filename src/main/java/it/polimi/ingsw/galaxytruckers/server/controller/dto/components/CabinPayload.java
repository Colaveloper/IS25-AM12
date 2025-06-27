package it.polimi.ingsw.galaxytruckers.server.controller.dto.components;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;

/**
 * Represents the payload for a cabin component in the Galaxy Truckers game.
 * This record contains the crew type and the number of residents in the cabin.
 *
 * @param crewType the type of crew in the cabin
 * @param numResidents the number of residents in the cabin
 */
public record CabinPayload(CrewType crewType, int numResidents) implements ComponentPayload {
}
