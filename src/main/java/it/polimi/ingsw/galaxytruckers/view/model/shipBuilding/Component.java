package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.observables.Invalidator;

import java.util.ArrayList;
import java.util.List;

public sealed class Component implements Invalidator permits
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

    private final List<Listener> listeners = new ArrayList<>();

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
    public void addObserver(Listener o) {
        listeners.add(o);
    }

    @Override
    public void removeObserver(Listener o) {
        listeners.remove(o);
    }

    public void notifyObservers() {
        for (Listener o : listeners) {
            o.onNotified();
        }
    }
}
