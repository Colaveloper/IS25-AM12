package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import java.util.ArrayList;
import java.util.List;

public class ComponentBank {
    private final int bankSize = 152;

    private int coveredComponentsN;
    private final List<Component> uncoveredComponents;

    public ComponentBank() {
        this.coveredComponentsN = bankSize;
        this.uncoveredComponents = new ArrayList<>();
    }

    public void removeUncoveredComponent(Component component) {
        uncoveredComponents.removeIf(uncoveredComponent -> uncoveredComponent.getId() == component.getId());
    }

    public void addUncoveredComponent(Component component) {
        uncoveredComponents.add(component);
    }

    public void removeCoveredComponent() {
        coveredComponentsN--;
    }

    public int getCoveredComponentsN() {
        return coveredComponentsN;
    }

    public List<Component> getUncoveredComponents() {
        return uncoveredComponents;
    }

    public void setCoveredComponentsN(int coveredComponentsN) {
        this.coveredComponentsN = coveredComponentsN;
    }

    public void setUncoveredComponents(List<Component> uncoveredComponents) {
        this.uncoveredComponents.clear();
        this.uncoveredComponents.addAll(uncoveredComponents);
    }
}