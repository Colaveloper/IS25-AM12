package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.Observer;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.ModelObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoodsBuffer implements ModelObservable {
    private final Map<GoodsType, Integer> goodsBuffer;

    private final List<Observer> observers = new ArrayList<>();

    public GoodsBuffer(Map<GoodsType, Integer> goodsBuffer) {
        this.goodsBuffer = goodsBuffer;
    }

    public Map<GoodsType, Integer> getGoodsBuffer() {
        return goodsBuffer;
    }

    public boolean isEmpty() {
        return goodsBuffer.isEmpty();
    }

    public void addToBuffer(GoodsType goodsType) {
        if (!goodsBuffer.containsKey(goodsType)) {
            goodsBuffer.put(goodsType, 0);
        }
        goodsBuffer.put(goodsType, goodsBuffer.get(goodsType) + 1);
        notifyObservers();
    }

    public void removeFromBuffer(GoodsType goodsType) {
        goodsBuffer.put(goodsType, goodsBuffer.get(goodsType) - 1);
        if (goodsBuffer.get(goodsType) == 0) {
            goodsBuffer.remove(goodsType);
        }
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

    protected void notifyObservers() {
        for (Observer o : observers) {
            o.onNotified();
        }
    }
}
