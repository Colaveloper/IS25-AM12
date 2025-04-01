package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.Physical;
import javafx.scene.image.Image;

import java.util.Collections;
import java.util.List;

public class Component implements Physical {
    private final List<Connector> connectors;
    // TODO: consider whether to make it an enum for clarity
    private int orientation;
    private final Image image;

    public Component(Image image, List<Connector> connectors) {
        this.image = image;
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
    }

    public void removeFromVisitor(ComponentVisitor visitor) {
    }

    @Override
    public Image getImage() {
        return image;
        // TODO: override and composite with batteries/goods/crew on top
    }

    @Override
    public String getDescription() {
        return "Component has connectors: "+connectors.size()+" and orientation "+orientation;
        // TODO: override
    }
}
