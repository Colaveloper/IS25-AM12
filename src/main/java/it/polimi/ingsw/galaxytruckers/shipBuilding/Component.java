package it.polimi.ingsw.galaxytruckers.shipBuilding;

import java.util.Collections;
import java.util.List;

public class Component {
    private final List<Connector> connectors;
    // TODO: consider whether to make it an enum for clarity
    private int orientation;

    public Component(List<Connector> connectors) {
        if (connectors == null || connectors.size() != 4) {
            throw new IllegalArgumentException("A component must have exactly 4 connectors");
        }
        this.connectors = connectors;
        this.orientation = 0;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

    public int getOrientation() {
        return orientation;
    }

    public void rotateLeft() {
        Collections.rotate(connectors, +1);
        orientation = (orientation+1) % 4;
    }

//    TODO: consider whether to remove this method (now untested)
//    public void rotateRight() {
//        Collections.rotate(connectors, -1);
//        orientation = (orientation-1) % 4;
//    }

    public void addToVisitor(ComponentVisitor visitor) {
        visitor.add(this);
    }

    public void removeFromVisitor(ComponentVisitor visitor) {
        visitor.remove(this);
    }
}
