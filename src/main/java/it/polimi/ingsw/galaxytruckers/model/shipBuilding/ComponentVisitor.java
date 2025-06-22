package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

public interface ComponentVisitor {
    void add(Cannon cannon);
    void add(Engine engine);
    void add(Battery battery);
    void add(Cabin cabin);
    void add(Shield shield);
    void add(LifeSupport lifeSupport);
    void add(CargoHold cargoHold);
    void add(DoubleCannon doubleCannon);
    void add(DoubleEngine doubleEngine);

    void remove(Cannon cannon);
    void remove(Engine engine);
    void remove(Battery battery);
    void remove(Cabin cabin);
    void remove(Shield shield);
    void remove(LifeSupport lifeSupport);
    void remove(CargoHold cargoHold);
    void remove(DoubleCannon doubleCannon);
    void remove(DoubleEngine doubleEngine);
}
