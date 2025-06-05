package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.view.observables.ObservableGeneric;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ComponentBank {
    private int coveredComponentsN;
    private final List<Component> uncoveredComponents;

    public ComponentBank(int coveredComponentsN) {
        this.coveredComponentsN = coveredComponentsN;
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
}