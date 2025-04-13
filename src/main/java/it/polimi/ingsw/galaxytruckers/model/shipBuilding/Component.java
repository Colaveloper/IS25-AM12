package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Component {
    private final List<Connector> connectors;
    // TODO: consider whether to make it an enum for clarity
    private int orientation;

    public Component(List<Connector> connectors) {
        this.connectors = connectors;
        this.orientation = 0;
    }

    public List<Connector> getConnectors() {
        List<Connector> res = new ArrayList<>();
        for (int i = 0; i < connectors.size(); i++) {
            res.add(connectors.get((i-orientation+connectors.size())%connectors.size()));
        }
        return res;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void addToVisitor(ComponentVisitor visitor) {
    }

    public void removeFromVisitor(ComponentVisitor visitor) {
    }
}
