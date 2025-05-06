package it.polimi.ingsw.galaxytruckers.view;

import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Physical {
    private boolean dirty;
    protected StackPane node;
    protected List<String> description;

    public void registerObservables(Observable... observables) {
        for (Observable o : observables) {
            o.addListener(obs -> dirty = true);
        }
    }

    public List<String> getDescription() {
        if (dirty || description == null) {
            description = getNewDescription();
            dirty = false;
        }
        return description;
    }

    public abstract List<String> getNewDescription() ;

    public StackPane getNode() {
        if (node==null) {
            node = new StackPane();
        }
        return node;
    }
}