package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ComponentBank {
    private int coveredComponentsN;
    private final List<Component> uncoveredComponents;

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
}