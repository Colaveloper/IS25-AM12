package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;

public abstract class Physical {
    private boolean dirty;
    protected StackPane node = new StackPane();;
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

    public abstract List<String> getNewDescription();

    public abstract Node getNode(VirtualServer server) throws IOException;
}