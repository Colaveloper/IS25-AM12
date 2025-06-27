package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import java.util.ArrayList;
import java.util.List;

/**
 * The bank of ship components available during ship construction.
 */
public class ComponentBank {
    private final int bankSize = 152;
    private int coveredComponentsN;
    private final List<Component> uncoveredComponents;

    /**
     * Constructs a new component bank.
     * Initializes the bank with all components covered (face-down).
     */
    public ComponentBank() {
        this.coveredComponentsN = bankSize;
        this.uncoveredComponents = new ArrayList<>();
    }

    /**
     * Removes a specific component from the uncovered pile.
     * Used when a player selects an uncovered component.
     *
     * @param component The component to remove from the uncovered pile
     */
    public void removeUncoveredComponent(Component component) {
        uncoveredComponents.removeIf(uncoveredComponent -> uncoveredComponent.getId() == component.getId());
    }

    /**
     * Adds a component to the uncovered pile.
     * Used when revealing new components for selection.
     *
     * @param component The component to add to the uncovered pile
     */
    public void addUncoveredComponent(Component component) {
        uncoveredComponents.add(component);
    }

    /**
     * Decrements the count of covered components.
     * Called when a covered component is drawn from the bank.
     */
    public void removeCoveredComponent() {
        coveredComponentsN--;
    }

    /**
     * Gets the number of components remaining in the covered pile.
     *
     * @return The number of face-down components
     */
    public int getCoveredComponentsN() {
        return coveredComponentsN;
    }

    /**
     * Gets the list of currently uncovered components.
     *
     * @return List of components that are face-up and available for selection
     */
    public List<Component> getUncoveredComponents() {
        return uncoveredComponents;
    }

    /**
     * Sets the number of covered components.
     * Used for game state synchronization.
     *
     * @param coveredComponentsN The new number of covered components
     */
    public void setCoveredComponentsN(int coveredComponentsN) {
        this.coveredComponentsN = coveredComponentsN;
    }

    /**
     * Sets the list of uncovered components.
     * Used for game state synchronization.
     *
     * @param uncoveredComponents The new list of uncovered components
     */
    public void setUncoveredComponents(List<Component> uncoveredComponents) {
        this.uncoveredComponents.clear();
        this.uncoveredComponents.addAll(uncoveredComponents);
    }
}