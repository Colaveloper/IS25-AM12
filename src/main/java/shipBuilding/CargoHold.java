package shipBuilding;

import enumTypes.GoodsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargoHold extends Component{
    private final int size;
    private int numGoods;
    private final Map<GoodsType, Integer> loot;

    public CargoHold(List<Connector> connectors, int size) {
        super(connectors);
        this.size = size;
        this.numGoods = 0;
        this.loot = new HashMap<>();
    }

    public Map<GoodsType, Integer> getLoot() {
        return loot;
    }

    public void addLoot(GoodsType goodsType, int amount) throws IllegalArgumentException {
        int updatedNumGoods = this.numGoods + amount;
        if (updatedNumGoods < this.size) {
            throw new IllegalArgumentException("Cannot add the goods because total capacity would be exceeded");
        }
        if (loot.containsKey(goodsType)) {
            loot.put(goodsType, loot.get(goodsType) + amount);
        } else {
            loot.put(goodsType, amount);
        }
        this.numGoods = updatedNumGoods;
    }

    public void removeLoot(GoodsType goodsType, int amount) throws IllegalArgumentException {
        if (!loot.containsKey(goodsType) || loot.get(goodsType) < amount) {
            throw new IllegalArgumentException("Cannot remove the goods there are not enough");
        }
        loot.put(goodsType, loot.get(goodsType) - amount);
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
