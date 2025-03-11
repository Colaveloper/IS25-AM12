package shipBuilding;

import java.util.List;

public class Cannon extends Component {

    public Cannon(List<Connector> connectors) {
        super(connectors);
    }

    public int getFirePower() {
        return (getOrientation() == 0) ? 2 : 1;
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
