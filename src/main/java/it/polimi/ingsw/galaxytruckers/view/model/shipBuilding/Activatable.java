package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

/**
 * Represents a ship component that can be activated and deactivated during gameplay.
 * This interface is sealed to only allow specific implementations: DoubleEngine, DoubleCannon, and Shield.
 * Components implementing this interface can have their state toggled to represent the activation status.
 */
public sealed interface Activatable permits DoubleEngine, DoubleCannon, Shield {
    /**
     * Checks if the component is currently active.
     *
     * @return true if the component is active, false otherwise
     */
    boolean isActive();

    /**
     * Sets the activation state of the component.
     *
     * @param active true to activate the component, false to deactivate it
     */
    void setActive(boolean active);
}
