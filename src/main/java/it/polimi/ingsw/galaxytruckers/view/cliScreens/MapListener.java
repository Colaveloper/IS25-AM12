package it.polimi.ingsw.galaxytruckers.view.cliScreens;

public interface MapListener<K, V> {
        void onPut(K key, V oldValue, V newValue);
        void onRemove(K key, V oldValue);
}
