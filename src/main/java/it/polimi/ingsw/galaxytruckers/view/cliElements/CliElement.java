package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.UiElement;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CliElement extends UiElement /*implements InvalidationListener, Observable*/ {
    private boolean dirty = true;
    protected final List<String> descriptionCache = new ArrayList<>();
    private InvalidationListener parent;

    public CliElement(ClientModel model) {
        super(model);
    }

//    @Override
//    public void invalidated(Observable o) {
//        dirty = true;
//        System.out.println("PHYSICAL DIRTY "+toString());
//        if (parent != null) {
//            System.out.println("PROPAGATING FROM "+getClass().getSimpleName());
//            parent.invalidated(this);  // Propagate invalidation to parent
//        }
//    }
//
//    @Override
//    public void addListener(InvalidationListener parent) {
//        this.parent = parent;
//    }
//
//    @Override
//    public void removeListener(InvalidationListener parent) {
//        this.parent = null;
//    }
//
//    public List<String> getDescription() {
//        if (dirty) {
//            System.out.println("REDESCRIBE "+toString());
//            descriptionCache.clear();
//            descriptionCache.addAll(getNewDescription());
//            dirty = false;
//        }
//        else {
//            System.out.println("USE CACHED DESCRIPTION "+toString());
//        }
//        return descriptionCache;
//    }

    protected abstract List<String> getNewDescription();
}