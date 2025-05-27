package it.polimi.ingsw.galaxytruckers.view.model;

import java.util.ArrayList;
import java.util.List;

public class ObservableProperty<T> {
    private final List<ModelObserver<T>> observers = new ArrayList<>();

    private T value;

    public ObservableProperty(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        notifyObservers();
    }

    private void notifyObservers() {
        for (ModelObserver<T> observer : observers) {
            observer.onNotified(value);
        }
    }

    public void addObserver(ModelObserver<T> o) {
        observers.add(o);
    }

    public void removeObserver(ModelObserver<T> o) {
        observers.remove(o);
    }
}
