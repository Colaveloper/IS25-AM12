package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

public interface Activatable {
    void activate(ActivatableVisitor visitor);
    void deactivate(ActivatableVisitor visitor);
    boolean isActive();
}
