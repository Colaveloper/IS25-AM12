package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.ComponentRegistry;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ComponentBank {
    private List<Component> coveredComponents;
    private final Map<Integer, Component> uncoveredComponents;

    public ComponentBank() {
        this.coveredComponents = new ArrayList<>();
        this.uncoveredComponents = new HashMap<>();
    }

    public void initialize() {
        this.coveredComponents = ComponentRegistry.getInstance().getBankComponents();
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

    @VisibleForTesting
    public List<Component> getCoveredComponents() {
        return new ArrayList<>(coveredComponents);
    }

    @VisibleForTesting
    public Map<Integer, Component> getUncoveredComponents() {
        return new HashMap<>(uncoveredComponents);
    }
}