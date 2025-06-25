package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

/**
 * Interface representing components that can be activated or deactivated in the ship building phase.
 * This interface is implemented by components such as DoubleCannon, DoubleEngine, and Shield.
 */
public sealed interface Activatable extends ComponentInterface permits DoubleCannon, DoubleEngine, Shield {
    void activate(ActivatableVisitor visitor);
    void deactivate(ActivatableVisitor visitor);
    boolean isActive();
}
