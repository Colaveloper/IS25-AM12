package shipBuilding;

import java.util.List;

public class Shield extends Component implements Activatable{
    public Shield(List<Connector> connectors) {
        super(connectors);
    }

    public int[] getDirections() {
        return new int[]{getOrientation(), (getOrientation()+1)%4};
    }

    @Override
    public void activate(ActivatableVisitor visitor) {
        visitor.activate(this);
    }

    @Override
    public void deactivate(ActivatableVisitor visitor) {
        visitor.deactivate(this);
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
