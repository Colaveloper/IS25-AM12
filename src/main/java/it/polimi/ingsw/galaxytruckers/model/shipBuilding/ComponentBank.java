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

    public void initialize() {
        this.coveredComponents.clear();
        this.coveredComponents.addAll(ComponentRegistry.getInstance().getBankComponents());
        Collections.shuffle(this.coveredComponents);
    }

    public Component removeUncoveredComponent(int id) {
        if (!uncoveredComponents.containsKey(id)) {
            throw new IllegalArgumentException("No component with id " + id + " exists");
        }
        return uncoveredComponents.remove(id);
    }

    public Component drawRandComponent() {
        return coveredComponents.removeLast();
    }

    public void addToUncoveredComponents(Component component) {
        uncoveredComponents.put(component.getId(), component);
    }

    public void returnCoveredComponent(Component component) {
        coveredComponents.add(component);
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