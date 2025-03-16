package it.polimi.ingsw.galaxytruckers.shipBuilding;

import java.util.*;

public class ComponentBank {
    private static ComponentBank instance;

    private final List<Component> coveredComponents;
    private final Map<Integer, Component> uncoveredComponents;

    ComponentBank() {
        //TODO: read components from file and shuffle them
        this.coveredComponents = new ArrayList<>();
        this.uncoveredComponents = new HashMap<>();
    }

    public static ComponentBank getInstance() {
        if (instance == null) {
            instance = new ComponentBank();
        }
        return instance;
    }

    public Component getComponent(int id) {
        if (!uncoveredComponents.containsKey(id)) {
            throw new IllegalArgumentException("No component with id " + id + " exists");
        }
        return uncoveredComponents.remove(id);
    }

    public Component getRanComponent() {
        return coveredComponents.removeLast();
    }

    public void addUncovered(Component component) {
        uncoveredComponents.put(component.hashCode(), component);
    }
}
