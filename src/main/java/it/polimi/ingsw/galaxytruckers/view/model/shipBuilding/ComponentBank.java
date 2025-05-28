package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.view.observables.ObservableGeneric;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;

public class ComponentBank {
    private final ObservableGeneric<Integer> coveredComponentsN;
    private final ObservableList<Component> uncoveredComponents;

    public ComponentBank(int coveredComponentsN) {
        this.coveredComponentsN = new ObservableGeneric<>(coveredComponentsN);
        this.uncoveredComponents = new ObservableList<>();
    }

    public void removeUncoveredComponent(Component component) {
        uncoveredComponents.remove(component);
    }

    public void addUncoveredComponent(Component component) {
        uncoveredComponents.add(component);
    }

    public ObservableList<Component> getUncoveredComponentsProperty() {
        return uncoveredComponents;
    }

    public void removeCoveredComponent() {
        coveredComponentsN.setValue(coveredComponentsN.getValue() - 1);
    }

    public ObservableGeneric<Integer> getCoveredComponentsNProperty() {
        return coveredComponentsN;
    }
}