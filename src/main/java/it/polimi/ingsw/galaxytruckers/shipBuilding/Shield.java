package it.polimi.ingsw.galaxytruckers.shipBuilding;

import java.util.List;

public class Shield extends Component implements Activatable{
    private boolean active;

    public Shield(List<Connector> connectors) {
        super(connectors);
        this.active = false;
    }

    public int[] getProtectedDirections() {
        if(this.active) {
            return new int[]{getOrientation(), (getOrientation()+1)%4};
        } else {
            return new int[]{};
        }
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
    public void addToVisitor(ComponentVisitor visitor) {
        visitor.add(this);
    }

    @Override
    public void removeFromVisitor(ComponentVisitor visitor) {
        visitor.remove(this);
    }
}
