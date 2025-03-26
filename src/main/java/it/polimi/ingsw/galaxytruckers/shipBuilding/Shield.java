package it.polimi.ingsw.galaxytruckers.shipBuilding;

import javafx.scene.image.Image;

import java.util.List;

public class Shield extends Component implements Activatable{
    private boolean active;

    public Shield(Image image, List<Connector> connectors) {
        super(image, connectors);
        this.active = false;
    }

    public int[] getDefensibleDirections() {
        return new int[]{getOrientation(), (getOrientation()+1)%4};
    }

    @Override
    public void activate(ActivatableVisitor visitor) {
        this.active = true;
        visitor.activate(this);
    }

    @Override
    public void deactivate(ActivatableVisitor visitor) {
        visitor.deactivate(this);
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return this.active;
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
