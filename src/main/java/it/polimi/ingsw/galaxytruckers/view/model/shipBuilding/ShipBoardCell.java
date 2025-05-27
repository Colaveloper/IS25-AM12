package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.view.Observer;
import it.polimi.ingsw.galaxytruckers.view.model.ModelObservable;

import java.util.ArrayList;
import java.util.List;

public class ShipBoardCell implements ModelObservable {
    private Component component;

    private final List<Observer> observers = new ArrayList<>();

    public ShipBoardCell() {
        this.component = null;
    }

    public ShipBoardCell(Component component) {
        this.component = component;
    }

    public void setComponent(Component component) {
        this.component = component;
        notifyObservers();
    }

    public Component getComponent() {
        return component;
    }

    public Component removeComponent() {
        Component component = this.component;
        this.component = null;
        notifyObservers();
        return component;
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
