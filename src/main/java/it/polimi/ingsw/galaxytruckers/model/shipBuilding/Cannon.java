package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

public non-sealed class Cannon extends Component implements ComponentInterface{

    public Cannon(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
    }

    @VisibleForTesting
    public Cannon(Map<Direction, Connector> connectors) {
        super(connectors);
    }

    @VisibleForTesting
    public Cannon() {
        super();
    }

    public int getFirePower() {
        return (getOrientation() == Direction.UP) ? 2 : 1;
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
