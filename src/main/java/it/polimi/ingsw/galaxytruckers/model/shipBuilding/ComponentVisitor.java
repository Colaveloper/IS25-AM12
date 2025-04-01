package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

public interface ComponentVisitor {
    public void add(Component component);
    public void add(Cannon cannon);
    public void add(Engine engine);
    public void add(Battery battery);
    public void add(Cabin cabin);
    public void add(Shield shield);
    public void add(LifeSupport lifeSupport);
    public void add(CargoHold cargoHold);
    public void add(DoubleCannon doubleCannon);
    public void add(DoubleEngine doubleEngine);


    public void remove(Component component);
    public void remove(Cannon cannon);
    public void remove(Engine engine);
    public void remove(Battery battery);
    public void remove(Cabin cabin);
    public void remove(Shield shield);
    public void remove(LifeSupport lifeSupport);
    public void remove(CargoHold cargoHold);
    public void remove(DoubleCannon doubleCannon);
    public void remove(DoubleEngine doubleEngine);
}
