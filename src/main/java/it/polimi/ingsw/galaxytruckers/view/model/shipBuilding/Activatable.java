package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

public sealed interface Activatable permits DoubleEngine, DoubleCannon, Shield{
    boolean isActive();
    void setActive(boolean active);
}
