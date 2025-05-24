package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestShipBoard extends ShipBoard {
    private final static Set<Point> shipArea = new HashSet<>(List.of(new Point(5, 7),
            new Point(5, 8),
            new Point(5, 9),
            new Point(6, 6),
            new Point(6, 7),
            new Point(6, 8),
            new Point(6, 9),
            new Point(7, 5),
            new Point(7, 6),
            new Point(7, 7),
            new Point(7, 8),
            new Point(7, 6),
            new Point(8, 6),
            new Point(8, 7),
            new Point(8, 8),
            new Point(8, 9),
            new Point(9, 7),
            new Point(9, 8),
            new Point(9, 9)));

    public TestShipBoard(FourColors color) {
        super(color);
    }

    public Set<Point> getShipArea() {
        return shipArea;
    }
}
