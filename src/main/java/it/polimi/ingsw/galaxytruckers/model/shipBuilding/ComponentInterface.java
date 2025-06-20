package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.view.Direction;

public sealed interface ComponentInterface permits Activatable, Battery, Cabin, Cannon, CargoHold, Component,
                                                   Engine, LifeSupport {
    int getId();
    Direction getOrientation();
}
