package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.Observer;
import it.polimi.ingsw.galaxytruckers.view.model.ModelObservable;

import java.util.ArrayList;
import java.util.List;

public abstract class CliElement implements ModelObservable, Observer {
    private boolean dirty = true;
    protected final List<String> descriptionCache = new ArrayList<>();
    private Observer parent;

    @Override
    public void addObserver(Observer parent) {
        this.parent = parent;
    }

    @Override
    public void removeObserver(Observer parent) {
        this.parent = null;
    }

    @Override // from ModelObservable
    public void notifyObservers() {
        dirty = true;
//        System.out.println("PHYSICAL DIRTY "+toString());
        if (parent != null) {
//            System.out.println("PROPAGATING FROM "+getClass().getSimpleName());
            parent.onNotified();  // Propagate invalidation to parent
        }
    }

    @Override // from Observer
    public void onNotified() {
        notifyObservers();
    }

    public List<String> getDescription() {
        if (dirty) {
//            System.out.println("REDESCRIBE "+toString());
            descriptionCache.clear();
            descriptionCache.addAll(getNewDescription());
            dirty = false;
        }
//        else {
//            System.out.println("USE CACHED DESCRIPTION "+toString());
//        }
        return descriptionCache;
    }

    protected abstract List<String> getNewDescription();
}