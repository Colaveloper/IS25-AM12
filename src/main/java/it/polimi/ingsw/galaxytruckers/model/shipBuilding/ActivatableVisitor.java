package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

public interface ActivatableVisitor {
    void activate(DoubleCannon doubleCannon);
    void activate(DoubleEngine doubleEngine);
    void activate(Shield shield);

    void deactivate(DoubleCannon doubleCannon);
    void deactivate(DoubleEngine doubleEngine);
    void deactivate(Shield shield);
}
