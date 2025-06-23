//package it.polimi.ingsw.galaxytruckers.view.observables;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * A generic implementation of the Observer pattern.
// * This class provides a way to observe changes to a value of type T.
// * Observers are notified whenever the value changes.
// *
// * @param <T> The type of the value being observed
// */
//public class ObservableGeneric<T> {
//    /**
//     * Interface for observers that want to be notified about changes to the value.
//     *
//     * @param <T> The type of the value being observed
//     */
//    public interface GenericObserver<T> {
//        /**
//         * Called when the observed value has changed.
//         *
//         * @param obj The new value
//         */
//        void onNotified(T obj);
//    }
//
//    private final List<GenericObserver<T>> observers = new ArrayList<>();
//
//    private T value;
//
//    /**
//     * Creates a new observable with the specified initial value.
//     *
//     * @param value The initial value
//     */
//    public ObservableGeneric(T value) {
//        this.value = value;
//    }
//
//    /**
//     * Gets the current value.
//     *
//     * @return The current value
//     */
//    public T getValue() {
//        return value;
//    }
//
//    /**
//     * Sets a new value and notifies all observers.
//     *
//     * @param value The new value
//     */
//    public void setValue(T value) {
//        this.value = value;
//        notifyObservers();
//    }
//
//    /**
//     * Notifies all observers about the value change.
//     */
//    private void notifyObservers() {
//        for (GenericObserver<T> observer : observers) {
//            observer.onNotified(value);
//        }
//    }
//
//    /**
//     * Adds an observer to be notified of value changes.
//     *
//     * @param o The observer to add
//     */
//    public void addObserver(GenericObserver<T> o) {
//        observers.add(o);
//    }
//
//    /**
//     * Removes an observer so it no longer receives notifications.
//     *
//     * @param o The observer to remove
//     */
//    public void removeObserver(GenericObserver<T> o) {
//        observers.remove(o);
//    }
//}
