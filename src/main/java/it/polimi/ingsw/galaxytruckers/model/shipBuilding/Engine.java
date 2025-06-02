package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.util.List;
import java.util.Map;

public class Engine extends Component {
    public Engine(Map<Direction, Connector> connectors, int id) {
        super(connectors, id);
    }

    @VisibleForTesting
    public Engine(Map<Direction, Connector> connectors) {
        super(connectors);
    }

    public boolean isValid() {
        return getOrientation() == Direction.UP;
    }

    public int getEnginePower() {
        return 1;
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
