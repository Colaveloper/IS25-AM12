package it.polimi.ingsw.galaxytruckers.view.observables;

import java.util.ArrayList;
import java.util.List;

public class ObservableGeneric<T> {
    public interface GenericObserver<T> {
        void onNotified(T obj);
    }

    private final List<GenericObserver<T>> observers = new ArrayList<>();

    private T value;

    public ObservableGeneric(T value) {
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
        for (GenericObserver<T> observer : observers) {
            observer.onNotified(value);
        }
    }

    public void addObserver(GenericObserver<T> o) {
        observers.add(o);
    }

    public void removeObserver(GenericObserver<T> o) {
        observers.remove(o);
    }
}
