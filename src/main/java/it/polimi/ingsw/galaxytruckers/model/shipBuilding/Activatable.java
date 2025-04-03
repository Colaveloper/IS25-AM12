package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

public interface Activatable {
    public void activate(ActivatableVisitor visitor);
    public void deactivate(ActivatableVisitor visitor);
    public boolean isActive();
}
