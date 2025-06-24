package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.HashMap;
import java.util.Map;

public non-sealed class CargoHold extends Component implements ComponentInterface{
    private final int size;
    private int numGoods;
    private final Map<GoodsType, Integer> goods;
    private final Boolean isSpecial;

    public CargoHold(Map<Direction, Connector> connectors, int id, Boolean isSpecial, int size) {
        super(connectors, id);
        this.isSpecial = isSpecial;
        this.size = size;
        this.goods = new HashMap<>();
        this.numGoods = 0;
    }

    @VisibleForTesting
    public CargoHold(Map<Direction, Connector> connectors, int size, Boolean isSpecial) {
        super(connectors);
        this.size = size;
        this.numGoods = 0;
        this.goods = new HashMap<>();
        this.isSpecial = isSpecial;
    }

    @VisibleForTesting
    public CargoHold(int size) {
        super();
        this.size = size;
        this.numGoods = 0;
        this.goods = new HashMap<>();
        this.isSpecial = false;
    }

    public Boolean getIsSpecial() { return isSpecial; }

    public Map<GoodsType, Integer> getGoods() {
        return new HashMap<>(goods);
    }

    public void addGoods(GoodsType goodsType, int amount) throws IllegalArgumentException {
        if (goodsType == GoodsType.RED && !isSpecial) {
            throw new IllegalArgumentException("Cannot add special goods in non-special CargoHold");
        }
        int updatedNumGoods = this.numGoods + amount;
        if (updatedNumGoods > this.size) {
            throw new IllegalArgumentException("Cannot add the goods because total capacity would be exceeded");
        }
        goods.merge(goodsType, amount, Integer::sum);
        this.numGoods = updatedNumGoods;
    }

    public void removeGoods(GoodsType goodsType, int amount) throws IllegalArgumentException {
        if (!goods.containsKey(goodsType) || goods.get(goodsType) < amount) {
            throw new IllegalArgumentException("Cannot remove the goods there are not enough");
        }
        goods.put(goodsType, goods.get(goodsType) - amount);
        if (goods.get(goodsType) <= 0) goods.remove(goodsType);
        this.numGoods -= amount;
    }

    @Override
    public void addToVisitor(ComponentVisitor visitor, java.awt.Point point) {
        visitor.add(this, point);
    }

    @Override
    public void removeFromVisitor(ComponentVisitor visitor, java.awt.Point point) {
        visitor.remove(this, point);
    }
}
