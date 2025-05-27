package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Observer;
import it.polimi.ingsw.galaxytruckers.view.model.ModelObservable;
import it.polimi.ingsw.galaxytruckers.view.model.ObservableProperty;

import java.util.ArrayList;
import java.util.List;

public sealed class Component implements ModelObservable permits
        Battery,
        Cabin,
        Cannon,
        CargoHold,
        Engine,
        LifeSupport,
        Shield {

    private final List<Connector> connectors;
    private int orientation;
    private final int id;

    private final List<Observer> observers = new ArrayList<>();

    public Component(List<Connector> connectors, int id) {
        this.connectors = connectors;
        this.orientation = 0;
        this.id = id;
    }

    public List<Connector> getConnectors() {
        List<Connector> res = new ArrayList<>();
        for (int i = 0; i < connectors.size(); i++) {
            res.add(connectors.get((i-orientation+connectors.size())%connectors.size()));
        }
        return res;
    }

    public int getId() {
        return id;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.onNotified();
        }
    }
}
