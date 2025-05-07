package it.polimi.ingsw.galaxytruckers.view.shipBuildingClient;

import it.polimi.ingsw.galaxytruckers.view.Physical;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentBank extends Physical {
    private final List<Component> revealedComponents;
    private final IntegerProperty coveredComponents;
    private Component currentComponent;
    private final List<Component> stashedComponents;

    public ComponentBank() {
        coveredComponents = new SimpleIntegerProperty();;   //TODO: placeholder, initiate at starting value or get from server?
        revealedComponents = new ArrayList<>();
        stashedComponents = new ArrayList<>();
        super.registerObservables(coveredComponents);
    }

    public void setStashedComponents(List<Integer> components) throws IOException {
        stashedComponents.clear();
        for (Integer componentId : components) {
            stashedComponents.add(new Component(0, componentId));
        }
    }

    public void addRevealedComponent(int componentId) throws IOException {
        revealedComponents.add(new Component(0, componentId));
    }

    public void removeRevealedComponent(int componentId) throws IOException {
        revealedComponents.remove(new Component(0, componentId));
    }

    public void removeStashedComponent(int componentId) throws IOException {
        stashedComponents.remove(new Component(0, componentId));
    }

    public void stashComponent(int componentId) throws IOException {
        stashedComponents.add(new Component(0, componentId));
    }

    public void setCoveredComponents(int coveredComponentsN) {
        this.coveredComponents.set(coveredComponentsN);
    }

    public void setCurrentComponent(int componentId) throws IOException {
        currentComponent = new Component(0, componentId);
    }

    public void clearCurrentComponent() {
        currentComponent = null;
    }

    public IntegerProperty coveredComponentsProperty() {
        return coveredComponents;
    }

    @Override
    public List<String> getNewDescription() {
        String padding = "  ";
        StringBuilder row = new StringBuilder();
        List<String> description = new ArrayList<>();

        description.add("Face down: " + coveredComponents.get());

        description.add("Face up: ");
        for (int i = 0; i < 3; i++) {
            for (Component component : revealedComponents) {
                row.append(component.getNewDescription().get(i));
                row.append(padding);
            }
            description.add(row.toString());
            row.setLength(0);
        }
        for (int n = 1; n <= revealedComponents.size(); n++) {
            row.append("  ").append(n).append("  ").append(padding);
        }
        description.add(row.toString());
        row.setLength(0);

        description.add("Stash: " + "\tHand: ");
        for (int i = 0; i < 3; i++) {
            for (Component component : stashedComponents) {
                row.append(component.getNewDescription().get(i));
                row.append(padding);
            }
            for (int n = 0; n < 2 - stashedComponents.size(); n++) {
                row.append("     ").append(padding);
            }
            row.append("\t\t\t");
            if (currentComponent != null) {
                row.append(currentComponent.getNewDescription().get(i));
            }
            description.add(row.toString());
            row.setLength(0);
        }
        for (int n = 0; n < stashedComponents.size(); n++) {
            row.append("  ").append((char) ('A' + n)).append("  ").append(padding);
        }
        description.add(row.toString());

        return description;
    }
}
