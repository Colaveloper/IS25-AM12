package it.polimi.ingsw.galaxytruckers.view.model;

public interface ModelObserver<T> {
    void notifyObservers(T obj);
}
