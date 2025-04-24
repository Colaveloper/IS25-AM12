package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import java.util.List;

public abstract class Physical {
    protected final StackPane node = new StackPane();

    public abstract List<String> getDescription();

    public StackPane getNode() {
        return node;
    }
}
