package it.polimi.ingsw.galaxytruckers.shipBuilding;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestShipBoard extends ShipBoard {
    private final static Set<Point> shipArea = new HashSet<Point>(List.of(new Point(5, 7),
            new Point(5, 8),
            new Point(5, 9),
            new Point(6, 6),
            new Point(6, 7),
            new Point(6, 8),
            new Point(6, 9),
            new Point(7, 5),
            new Point(7, 6),
            new Point(7, 7),
            new Point(7, 8),
            new Point(7, 6),
            new Point(8, 6),
            new Point(8, 7),
            new Point(8, 8),
            new Point(8, 9),
            new Point(9, 7),
            new Point(9, 8),
            new Point(9, 9)));

    public TestShipBoard(ComponentBank componentBank, Colors color) {
        super(componentBank, color);
    }

    public TestShipBoard(Colors color) {
        super(color);
    }

    @Override
    protected boolean containsPoint(Point point) {
        return shipArea.contains(point);
    }

    //Visitor pattern methods

    @Override
    public void activate(DoubleCannon doubleCannon) {
        this.firePower += doubleCannon.getFirePower();
    }

    @Override
    public void activate(DoubleEngine doubleEngine) {
        this.enginePower += doubleEngine.getEnginePower();
    }

    @Override
    public void activate(Shield shield) {
        for (int direction : shield.getProtectedDirections()) {
            this.shieldDirections[direction] += 1;
        }
    }

    @Override
    public void deactivate(DoubleCannon doubleCannon) {
        this.firePower -= doubleCannon.getFirePower();
    }

    @Override
    public void deactivate(DoubleEngine doubleEngine) {
        this.enginePower -= doubleEngine.getEnginePower();
    }

    @Override
    public void deactivate(Shield shield) {
        for (int direction : shield.getProtectedDirections()) {
            this.shieldDirections[direction] -= 1;
        }
    }

    @Override
    public void add(Component component) {}

    @Override
    public void add(Cannon cannon) {
        this.cannons.put(this.lastPosition, cannon);
        this.firePower += cannon.getFirePower();
    }

    @Override
    public void add(Engine engine) {
        this.engines.put(this.lastPosition, engine);
        this.enginePower += engine.getEnginePower();
    }

    @Override
    public void add(Battery battery) {
        this.batteries.put(this.lastPosition, battery);
        this.numBatteries += battery.getNumBatteries();
    }

    @Override
    public void add(Cabin cabin) {
        this.cabins.put(this.lastPosition, cabin);
    }

    @Override
    public void add(Shield shield) {
        this.shields.put(this.lastPosition, shield);
    }

    @Override
    public void add(LifeSupport lifeSupport) {}

    @Override
    public void add(CargoHold cargoHold) {
        this.cargoHolds.put(this.lastPosition, cargoHold);
    }

    @Override
    public void add(DoubleCannon doubleCannon) {
        this.cannons.put(this.lastPosition, doubleCannon);
        this.activatables.put(this.lastPosition, doubleCannon);
    }

    @Override
    public void add(DoubleEngine doubleEngine) {
        this.engines.put(this.lastPosition, doubleEngine);
        this.activatables.put(this.lastPosition, doubleEngine);
    }

    @Override
    public void remove(Component component) {}

    @Override
    public void remove(Cannon cannon) {
        this.cannons.remove(this.lastPosition);
        this.firePower -= cannon.getFirePower();
    }

    @Override
    public void remove(Engine engine) {
        this.engines.remove(this.lastPosition);
        this.enginePower -= engine.getEnginePower();
    }

    @Override
    public void remove(Battery battery) {
        this.batteries.remove(this.lastPosition);
        this.numBatteries -= battery.getNumBatteries();
    }

    @Override
    public void remove(Cabin cabin) {
        this.cabins.remove(this.lastPosition);
        this.crewSize -= cabin.getNumResidents();
    }

    @Override
    public void remove(Shield shield) {
        shield.deactivate(this);
        this.shields.remove(this.lastPosition);
    }

    @Override
    public void remove(LifeSupport lifeSupport) {}

    @Override
    public void remove(CargoHold cargoHold) {
        Map<GoodsType, Integer> lostGoods = this.cargoHolds.get(lastPosition).getGoods();
        for (GoodsType goods: lostGoods.keySet()) {
            this.goods.put(goods, this.goods.get(goods) - lostGoods.get(goods));
        }
        this.cargoHolds.remove(this.lastPosition);
    }

    @Override
    public void remove(DoubleCannon doubleCannon) {
        doubleCannon.deactivate(this);
        this.cannons.remove(this.lastPosition);
        this.activatables.remove(this.lastPosition);
    }

    @Override
    public void remove(DoubleEngine doubleEngine) {
        doubleEngine.deactivate(this);
        this.engines.remove(this.lastPosition);
        this.activatables.remove(this.lastPosition);
    }
}
