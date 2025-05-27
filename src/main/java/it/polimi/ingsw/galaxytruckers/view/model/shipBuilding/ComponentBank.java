package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Observer;
import it.polimi.ingsw.galaxytruckers.view.model.ModelObservable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ComponentBank implements ModelObservable {
    private int coveredComponentsN;
    private final List<Component> uncoveredComponents;

    private final List<Observer> observers = new ArrayList<>();

    public ComponentBank(int coveredComponentsN) {
        this.coveredComponentsN = coveredComponentsN;
        this.uncoveredComponents = new ArrayList<>();
    }

    public void removeUncoveredComponent(Component component) {
        uncoveredComponents.remove(component);
    }

    public void addUncoveredComponent(Component component) {
        uncoveredComponents.add(component);
    }

    public List<Component> getUncoveredComponents() {
        return uncoveredComponents;
    }

    public void removeCoveredComponent() {
        this.coveredComponentsN--;
    }

    public int getCoveredComponentsN() {
        return coveredComponentsN;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.onNotified();
        }
    }
}