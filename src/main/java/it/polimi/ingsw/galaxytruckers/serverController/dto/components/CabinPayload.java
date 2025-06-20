package it.polimi.ingsw.galaxytruckers.serverController.dto.components;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

/**
 * Additional parameters for {@link it.polimi.ingsw.galaxytruckers.serverController.dto.ComponentDTO}
 * instances of type {@link it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin}
 * @param crewType cabin's crew type
 * @param numResidents cabin's number of residents
 */
public record CabinPayload(CrewType crewType, int numResidents) implements ComponentPayload {
}
