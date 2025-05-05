package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;

import java.util.List;

public class Engine extends Component {
    public Engine(List<Connector> connectors, int id) {
        super(connectors, id);
    }

    @VisibleForTesting
    public Engine(List<Connector> connectors) {
        super(connectors);
    }

    public boolean isValid() {
        return getOrientation() == 0;
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
