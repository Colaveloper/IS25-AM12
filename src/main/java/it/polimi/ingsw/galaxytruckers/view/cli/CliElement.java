package it.polimi.ingsw.galaxytruckers.view.cli;

import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CliElement implements InvalidationListener, Observable {
    protected ClientModel model;
    private boolean dirty = true;
    protected final List<String> descriptionCache = new ArrayList<>();
    private InvalidationListener parent;

    public CliElement(ClientModel model) {
        this.model = model;
    }

    public void listenToInvalidation(Observable p) {
        p.addListener(this);
    }

    @Override
    public void addListener(InvalidationListener parent) {
        this.parent = parent;
    }

    @Override
    public void removeListener(InvalidationListener parent) {
        this.parent = null;
    }

    @Override
    public void invalidated(Observable o) {
        try {
            invalidate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void invalidate() throws IOException {
        dirty = true;
//        System.out.println("PHYSICAL DIRTY "+getClass().getSimpleName());
        if (parent != null) {
            parent.invalidated(this);  // Propagate invalidation to parent
        }
    }

    public List<String> getDescription() throws IOException {
        if (dirty) {
//            System.out.println("REDESCRIBE "+getClass().getSimpleName());
            descriptionCache.clear();
            descriptionCache.addAll(getNewDescription());
            dirty = false;
        }
//        else {
//            System.out.println("USE CACHED DESCRIPTION "+getClass().getSimpleName());
//        }
        return descriptionCache;
    }

    public abstract List<String> getNewDescription() throws IOException;
}