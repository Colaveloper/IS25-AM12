package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentBank extends Physical {
    private List<Component> revealedComponents;
    private int coveredComponents;
    private List<String> description;
    private Component currentComponent;
    private List<Component> stashedComponents;

    public ComponentBank() {
        this.coveredComponents = 999;   //TODO: placeholder, initiate at starting value or get from server?
        this.revealedComponents = new ArrayList<>();
        this.stashedComponents = new ArrayList<>();
    }

    public void setStashedComponents(List<Integer> components) throws IOException {
        stashedComponents = new ArrayList<>();
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

    public void setCurrentComponent(int componentId) throws IOException {
        currentComponent = new Component(0, componentId);
    }

    public void clearCurrentComponent() {
        currentComponent = null;
    }

    @Override
    public List<String> getDescription() {
        String padding = "  ";
        StringBuilder row = new StringBuilder();
        description = new ArrayList<>();

        description.add("Face down: " + coveredComponents);

        description.add("Face up: ");
        for (int i = 0; i < 3; i++) {
            for (Component component : revealedComponents) {
                row.append(component.getDescription().get(i));
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
                row.append(component.getDescription().get(i));
                row.append(padding);
            }
            for (int n = 0; n < 2 - stashedComponents.size(); n++) {
                row.append("     ").append(padding);
            }
            row.append("\t\t\t");
            if (currentComponent != null) {
                row.append(currentComponent.getDescription().get(i));
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
