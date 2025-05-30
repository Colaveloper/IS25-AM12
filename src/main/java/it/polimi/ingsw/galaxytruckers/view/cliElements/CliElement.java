package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.observables.Invalidator;

import java.util.ArrayList;
import java.util.List;

public abstract class CliElement {
    private boolean dirty = true;
    protected final List<String> descriptionCache = new ArrayList<>();

    public void setDirty() {
        this.dirty = true;
    }

    public List<String> getDescription() {
        if (dirty) {
            System.out.println("REDESCRIBE "+toString());
            descriptionCache.clear();
            descriptionCache.addAll(getNewDescription());
            dirty = false;
        }
        else {
            System.out.println("USE CACHED DESCRIPTION "+toString());
        }
        return descriptionCache;
    }

    protected abstract List<String> getNewDescription();
}