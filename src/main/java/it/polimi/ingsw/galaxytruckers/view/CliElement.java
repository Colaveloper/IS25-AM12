package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class CliElement implements ChangeListener {
    private final AtomicBoolean dirty = new AtomicBoolean(true);
    protected final List<String> descriptionCache = new ArrayList<>();
    private ChangeListener listener;

    protected void registerObservables(Observable... observables) {
        for (Observable o : observables) {
            o.addListener(obs -> {
                try {
                    markDirty();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void markDirty() throws IOException {
        dirty.set(true);
//        System.out.println("PHYSICAL DIRTY "+getClass().getSimpleName());
        if (listener != null) {
            listener.onChanged();
        }
    }

    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onChanged() throws IOException {
//        System.out.println("PROPAGATING UPDATE "+getClass().getSimpleName());
        markDirty();
    }

    public List<String> getDescription() {
        if (dirty.get()) {
//            System.out.println("REDESCRIBE "+getClass().getSimpleName());
            descriptionCache.clear();
            descriptionCache.addAll(getNewDescription());
            dirty.set(false);
        } else {
//            System.out.println("USE CACHED DESCRIPTION "+getClass().getSimpleName());
        }
        return descriptionCache;
    }

    public abstract List<String> getNewDescription();
}