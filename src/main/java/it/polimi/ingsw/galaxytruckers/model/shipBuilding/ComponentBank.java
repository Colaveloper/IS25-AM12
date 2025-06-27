package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.ComponentRegistry;

import java.util.*;

/**
 * Represents a bank of components used in the Galaxy Truckers game during
 * ship building.
 */
public class ComponentBank {
    private final List<Component> coveredComponents;
    private final Map<Integer, Component> uncoveredComponents;

    /**
     * Constructor for ComponentBank. initializes the lists for covered and uncovered components
     * as empty collections.
     */
    public ComponentBank() {
        this.coveredComponents = new ArrayList<>();
        this.uncoveredComponents = new HashMap<>();
    }

    /**
     * Initializes the component bank with all the components from the ComponentRegistry.
     * It shuffles the covered components to randomize their order.
     */
    public void initialize() {
        this.coveredComponents.clear();
        this.coveredComponents.addAll(ComponentRegistry.getInstance().getBankComponents());
        Collections.shuffle(this.coveredComponents);
    }

    /**
     * Removes a component from the uncovered components by its ID.
     *
     * @param id the ID of the component to remove
     * @return the removed component
     */
    public Component removeUncoveredComponent(int id) {
        if (!uncoveredComponents.containsKey(id)) {
            throw new IllegalArgumentException("No component with id " + id + " exists");
        }
        return uncoveredComponents.remove(id);
    }

    /**
     * Draws a random component from the covered components.
     * If there are no covered components left, it throws an exception.
     *
     * @return A randomly drawn component.
     */
    public Component drawRandComponent() {
        return coveredComponents.removeLast();
    }

    /**
     * Adds a component to the uncovered components.
     *
     * @param component the component to add
     */
    public void addToUncoveredComponents(Component component) {
        uncoveredComponents.put(component.getId(), component);
    }

    /**
     * Returns a component to the covered components.
     * This method is used when a component wasn't really available for drawing
     *
     * @param component the component to return
     */
    public void returnCoveredComponent(Component component) {
        coveredComponents.add(component);
    }

    /**
     * @return the number of covered components in the bank.
     */
    public int getNumCovered() {
        return coveredComponents.size();
    }

    /**
     * @return a list of IDs of uncovered components.
     */
    public List<Integer> getUncoveredIds() {
        return new ArrayList<>(uncoveredComponents.keySet());
    }

    /**
     * For testing only.
     *
     * @return a list of all covered components.
     */
    @VisibleForTesting
    public List<Component> getCoveredComponents() {
        return new ArrayList<>(coveredComponents);
    }

    /**
     * For testing only.
     *
     * @return a map of uncovered components, where the key is the component ID and the value is the component itself.
     */
    @VisibleForTesting
    public Map<Integer, Component> getUncoveredComponents() {
        return new HashMap<>(uncoveredComponents);
    }
}