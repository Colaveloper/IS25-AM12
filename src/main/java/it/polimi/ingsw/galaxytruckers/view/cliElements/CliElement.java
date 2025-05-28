package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.observables.Invalidator;

import java.util.ArrayList;
import java.util.List;

public abstract class CliElement implements Invalidator, Invalidator.Listener {
    private boolean dirty = true;
    protected final List<String> descriptionCache = new ArrayList<>();
    private Listener parent;

    @Override
    public void addObserver(Listener parent) {
        this.parent = parent;
    }

    @Override
    public void removeObserver(Listener parent) {
        this.parent = null;
    }

    @Override // from Invalidator
    public void notifyObservers() {
        dirty = true;
//        System.out.println("PHYSICAL DIRTY "+toString());
        if (parent != null) {
//            System.out.println("PROPAGATING FROM "+getClass().getSimpleName());
            parent.onNotified();  // Propagate invalidation to parent
        }
    }

    @Override // from Listener
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