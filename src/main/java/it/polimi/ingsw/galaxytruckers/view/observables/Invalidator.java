package it.polimi.ingsw.galaxytruckers.view.observables;

public interface Invalidator {
    interface Listener {
        void onNotified();
    }
    void addObserver(Listener o);
    void removeObserver(Listener o);
    void notifyObservers();
}
