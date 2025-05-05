package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Component {
    private final List<Connector> connectors;
    // TODO: consider whether to make it an enum for clarity
    private int orientation;
    private final int id;

    public Component(List<Connector> connectors, int id) {
        this.connectors = connectors;
        this.orientation = 0;
        this.id = id;
    }

    @VisibleForTesting
    public Component(List<Connector> connectors) {
        this.connectors = connectors;
        this.orientation = 0;
        this.id = 0;
    }

    public List<Connector> getConnectors() {
        List<Connector> res = new ArrayList<>();
        for (int i = 0; i < connectors.size(); i++) {
            res.add(connectors.get((i-orientation+connectors.size())%connectors.size()));
        }
        return res;
    }

    public int getId() {
        return id;
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
