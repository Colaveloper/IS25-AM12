package it.polimi.ingsw.galaxytruckers.view.observables;

import java.util.ArrayList;
import java.util.List;

public class ObservableList<T> {
    public interface Listener<T> {
        void onAdd(int index, T element);
        void onRemove(int index, T element);
    }

    private final List<T> internalList = new ArrayList<>();
    private final List<Listener<T>> listeners = new ArrayList<>();

    public void addListener(Listener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<T> listener) {
        listeners.remove(listener);
    }

    public boolean add(T element) {
        boolean added = internalList.add(element);
        if (added) {
            int index = internalList.size() - 1;
            for (Listener<T> listener : listeners) {
                listener.onAdd(index, element);
            }
        }
        return added;
    }

    public void add(int index, T element) {
        internalList.add(index, element);
        for (Listener<T> listener : listeners) {
            listener.onAdd(index, element);
        }
    }

    public T remove(int index) {
        T removed = internalList.remove(index);
        for (Listener<T> listener : listeners) {
            listener.onRemove(index, removed);
        }
        return removed;
    }

    public boolean remove(T element) {
        int index = internalList.indexOf(element);
        if (index >= 0) {
            internalList.remove(index);
            for (Listener<T> listener : listeners) {
                listener.onRemove(index, element);
            }
            return true;
        }
        return false;
    }

    public T get(int index) {
        return internalList.get(index);
    }

    public int size() {
        return internalList.size();
    }

    public List<T> getUnmodifiableView() {
        return List.copyOf(internalList);
    }
}
