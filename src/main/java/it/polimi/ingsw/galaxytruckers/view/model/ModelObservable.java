package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.view.Observer;

public interface ModelObservable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
}
