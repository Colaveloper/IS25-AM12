package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

/**
 * Interface representing components that can be activated or deactivated in the ship building phase.
 * This interface is implemented by components such as DoubleCannon, DoubleEngine, and Shield.
 */
public sealed interface Activatable extends ComponentInterface permits DoubleCannon, DoubleEngine, Shield {
    /**
     * Activates the component.
     */
    void activate();
    /**
     * Deactivates the component.
     */
    void deactivate();
    /**
     * Checks if the component is currently active.
     *
     * @return true if the component is active, false otherwise.
     */
    boolean isActive();
}
