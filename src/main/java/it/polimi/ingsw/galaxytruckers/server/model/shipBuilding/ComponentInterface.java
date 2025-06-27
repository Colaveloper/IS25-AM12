package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

/**
 * Marker interface for all components in the Galaxy Truckers game.
 */
public sealed interface ComponentInterface permits Activatable, Battery, Cabin, Cannon, CargoHold, Component,
                                                   Engine, LifeSupport {
    /**
     * @return the unique identifier of the component
     */
    int getId();

    /**
     * @return the orientation of the component
     */
    Direction getOrientation();
}
