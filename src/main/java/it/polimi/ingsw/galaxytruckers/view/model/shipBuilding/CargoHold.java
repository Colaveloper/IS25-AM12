package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CargoHold extends Component {
    private final int size;
    private final Map<GoodsType, Integer> goods;
    private final boolean isSpecial;

    public CargoHold(List<Connector> connectors, int id, Boolean isSpecial, int size) {
        super(connectors, id);
        this.isSpecial = isSpecial;
        this.size = size;
        this.goods = new HashMap<>();
    }

    public int getSize() {
        return size;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public Map<GoodsType, Integer> getGoods() {
        return new HashMap<>(goods);
    }

    public void addGoods(GoodsType goodsType) {
        if (!goods.containsKey(goodsType)) {
            goods.put(goodsType, 0);
        }
        goods.put(goodsType, goods.get(goodsType) + 1);
        notifyObservers();
    }

    public void removeGoods(GoodsType goodsType) throws IllegalArgumentException {
        if (goods.containsKey(goodsType)) {
            goods.put(goodsType, goods.get(goodsType) - 1);
            if (goods.get(goodsType) == 0) {
                goods.remove(goodsType);
            }
        }
        notifyObservers();
    }

}
