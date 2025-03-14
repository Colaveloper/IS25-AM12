package it.polimi.ingsw.galaxytruckers.shipBuilding;

import java.util.Deque;
import java.util.Map;

public class ComponentBank {
    private Deque<Component> coveredComponents;
    private Map<Integer, Component> uncoveredComponents;

    public Component getComponent(int id) {
        return uncoveredComponents.remove(id);
    }

    public Component getRanComponent() {
        return coveredComponents.poll();
    }

    public void addUncovered(Component component) {
        uncoveredComponents.put(component.hashCode(), component);
    }
}
