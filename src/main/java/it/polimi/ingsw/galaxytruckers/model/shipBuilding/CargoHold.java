package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargoHold extends Component{
    private final int size;
    private int numGoods;
    private final Map<GoodsType, Integer> goods;
    private final Boolean isSpecial;

    public CargoHold(List<Connector> connectors, int size, Boolean isSpecial) {
        super(connectors);
        this.size = size;
        this.numGoods = 0;
        this.goods = new HashMap<>();
        this.isSpecial = isSpecial;
    }

    public Boolean getIsSpecial() { return isSpecial; }

    public Map<GoodsType, Integer> getGoods() {
        return goods;
    }

    public void addGoods(GoodsType goodsType, int amount) throws IllegalArgumentException {
        if (goodsType == GoodsType.RED && !isSpecial) {
            throw new IllegalArgumentException("Cannot add special goods in non-special CargoHold");
        }
        int updatedNumGoods = this.numGoods + amount;
        if (updatedNumGoods > this.size) {
            throw new IllegalArgumentException("Cannot add the goods because total capacity would be exceeded");
        }
        if (goods.containsKey(goodsType)) {
            goods.put(goodsType, goods.get(goodsType) + amount);
        } else {
            goods.put(goodsType, amount);
        }
        this.numGoods = updatedNumGoods;
    }

    public void removeGoods(GoodsType goodsType, int amount) throws IllegalArgumentException {
        if (!goods.containsKey(goodsType) || goods.get(goodsType) < amount) {
            throw new IllegalArgumentException("Cannot remove the goods there are not enough");
        }
        goods.put(goodsType, goods.get(goodsType) - amount);
        this.numGoods -= amount;
    }

    @Override
    public void addToVisitor(ComponentVisitor visitor) {
        visitor.add(this);
    }

    @Override
    public void removeFromVisitor(ComponentVisitor visitor) {
        visitor.remove(this);
    }
}
