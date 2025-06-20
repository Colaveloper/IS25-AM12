package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

public sealed interface Activatable extends ComponentInterface permits DoubleCannon, DoubleEngine, Shield {
    void activate(ActivatableVisitor visitor);
    void deactivate(ActivatableVisitor visitor);
    boolean isActive();
}
