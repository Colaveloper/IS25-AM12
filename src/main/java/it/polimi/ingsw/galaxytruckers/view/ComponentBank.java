package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentBank implements Physical {
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
    public Image getImage() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        StringBuilder row = new StringBuilder();
        description = new ArrayList<>();

        description.add("Hidden components on the board: " + coveredComponents);

        description.add("Revealed components on the board: ");
        for (int i = 0; i < 3; i++) {
            for (Component component : revealedComponents) {
                row.append(component.getDescription().get(i));
            }
            description.add(row.toString());
            row.setLength(0);
        }
        for (int n = 1; n <= revealedComponents.size(); n++) {
            row.append("  ").append(n).append("  ");
        }
        description.add(row.toString());
        row.setLength(0);

        description.add("Stashed components: ");
        for (int i = 0; i < 3; i++) {
            for (Component component : stashedComponents) {
                row.append(component.getDescription().get(i));
            }
            description.add(row.toString());
            row.setLength(0);
        }
        for (int n = 1; n <= stashedComponents.size(); n++) { // TODO: use letters
            row.append("  ").append(n).append("  ");
        }
        description.add(row.toString());

        if(currentComponent != null) {
            description.add("Current component: ");
            description.addAll(currentComponent.getDescription());
        }

        return description;
    }
}
