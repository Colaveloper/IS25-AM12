package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import java.awt.Point;

public interface ComponentVisitor {
    void add(Cannon cannon, Point position);
    void add(Engine engine, Point position);
    void add(Battery battery, Point position);
    void add(Cabin cabin, Point position);
    void add(Shield shield, Point position);
    void add(LifeSupport lifeSupport, Point position);
    void add(CargoHold cargoHold, Point position);
    void add(DoubleCannon doubleCannon, Point position);
    void add(DoubleEngine doubleEngine, Point position);

    void remove(Cannon cannon, Point position);
    void remove(Engine engine, Point position);
    void remove(Battery battery, Point position);
    void remove(Cabin cabin, Point position);
    void remove(Shield shield, Point position);
    void remove(LifeSupport lifeSupport, Point position);
    void remove(CargoHold cargoHold, Point position);
    void remove(DoubleCannon doubleCannon, Point position);
    void remove(DoubleEngine doubleEngine, Point position);
}
