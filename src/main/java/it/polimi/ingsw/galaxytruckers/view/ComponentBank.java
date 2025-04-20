package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.util.List;

public class ComponentBank implements Physical {
    private List<Component> revealedComponents;
    private List<String> description;

    public ComponentBank() {

    }


    public void addRevealedComponent(Component component) {
        revealedComponents.add(component);
    }

    public Component getComponent(int index) {
        return revealedComponents.remove(index);
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        StringBuilder row = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            for (Component component : revealedComponents) {
                row.append(component.getDescription().get(i));
            }
            description.add(row.toString());
        }

        return description;
    }
}
