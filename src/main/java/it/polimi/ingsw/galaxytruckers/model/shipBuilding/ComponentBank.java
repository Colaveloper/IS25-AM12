package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.ComponentRegistry;

import java.util.*;

public class ComponentBank {
    private final List<Component> coveredComponents;
    private final Map<Integer, Component> uncoveredComponents;

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

    public int getNumCovered() {
        return coveredComponents.size();
    }

    public List<Integer> getUncoveredIds() {
        return new ArrayList<>(uncoveredComponents.keySet());
    }

    @VisibleForTesting
    public List<Component> getCoveredComponents() {
        return new ArrayList<>(coveredComponents);
    }

    @VisibleForTesting
    public Map<Integer, Component> getUncoveredComponents() {
        return new HashMap<>(uncoveredComponents);
    }
}