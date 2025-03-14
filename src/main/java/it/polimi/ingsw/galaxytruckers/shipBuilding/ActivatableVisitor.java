package it.polimi.ingsw.galaxytruckers.shipBuilding;

public interface ActivatableVisitor {
    public void activate(DoubleCannon doubleCannon);
    public void activate(DoubleEngine doubleEngine);
    public void activate(Shield shield);

    public void deactivate(DoubleCannon doubleCannon);
    public void deactivate(DoubleEngine doubleEngine);
    public void deactivate(Shield shield);
}
