package it.polimi.ingsw.galaxytruckers.view.cliElements;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic CLI element that can be described.
 * It caches the description to avoid recomputing it unnecessarily.
 */
public abstract class CliElement {

    private boolean dirty = true;
    protected final List<String> descriptionCache = new ArrayList<>();

    /**
     * Marks this element as dirty, indicating that its description needs to be recomputed.
     */
    public void setDirty() {
        this.dirty = true;
    }

    /**
     * Returns the cached description of this element.
     * If the description is dirty, it recomputes it and updates the cache,
     * calling {@link #getNewDescription()} to get the new description.
     *
     * @return A list of strings representing the description of this element
     */
    public final List<String> getDescription() {
        if (dirty) {
            //System.out.println("REDESCRIBE "+toString());
            descriptionCache.clear();
            descriptionCache.addAll(getNewDescription());
            dirty = false;
        }
        return descriptionCache;
    }

    /**
     * Abstract method to be implemented by subclasses to provide the new description.
     * This method is called when the description is dirty and needs to be recomputed.
     *
     * @return A list of strings representing the new description of this element
     */
    protected abstract List<String> getNewDescription();
}