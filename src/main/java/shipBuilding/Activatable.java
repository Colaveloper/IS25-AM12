package shipBuilding;

public interface Activatable {
    public void activate(ActivatableVisitor visitor);
    public void deactivate(ActivatableVisitor visitor);
}
