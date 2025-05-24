package it.polimi.ingsw.galaxytruckers.view.model;

import java.awt.*;

public class UnweldedComponent {
    private Boolean exists;
    private Boolean isHand;
    private Point position;

    public UnweldedComponent(Boolean exists, Boolean isHand, Point position) {
        this.exists = exists;
        this.isHand = isHand;
        this.position = position;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Boolean getIsHand() {
        return isHand;
    }

    public void setIsHand(Boolean hand) {
        isHand = hand;
    }
}
