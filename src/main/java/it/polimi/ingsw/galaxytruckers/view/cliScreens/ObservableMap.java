package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import java.util.*;
import java.util.function.BiConsumer;

public class ObservableMap<K, V> {
    private final Map<K, V> map = new HashMap<>();

    private final List<MapListener<K, V>> listeners = new ArrayList<>();

    public void addListener(MapListener<K, V> listener) {
        listeners.add(listener);
    }

    public void removeListener(MapListener<K, V> listener) {
        listeners.remove(listener);
    }

    public V put(K key, V value) {
        V old = map.put(key, value);
        for (MapListener<K, V> l : listeners) {
            l.onPut(key, old, value);
        }
        return old;
    }

    public V remove(K key) {
        V old = map.remove(key);
        if (old != null) {
            for (MapListener<K, V> l : listeners) {
                l.onRemove(key, old);
            }
        }
        return old;
    }

    public V get(K key) {
        return map.get(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            for (MapListener<K, V> l : listeners) {
                l.onRemove(entry.getKey(), entry.getValue());
            }
        }
        map.clear();
    }
}
