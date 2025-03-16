package it.polimi.ingsw.galaxytruckers.shipBuilding;

import java.util.Deque;
import java.util.List;
import java.util.Map;

public class ComponentBank {
    private List<Component> coveredComponents;
    private Map<Integer, Component> uncoveredComponents;

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
