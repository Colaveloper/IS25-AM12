package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;

import java.awt.*;
import java.util.List;
import java.util.*;

public class SecondShipBoard extends ShipBoard {
    private static final Set<Point> shipArea = new HashSet<>(List.of(new Point(4, 7),
            new Point(4, 8),
            new Point(4, 9),
            new Point(5, 6),
            new Point(5, 7),
            new Point(5, 8),
            new Point(5, 9),
            new Point(6, 5),
            new Point(6, 6),
            new Point(6, 7),
            new Point(6, 8),
            new Point(6, 9),
            new Point(7, 6),
            new Point(7, 7),
            new Point(7, 8),
            new Point(8, 5),
            new Point(8, 6),
            new Point(8, 7),
            new Point(8, 8),
            new Point(8, 9),
            new Point(9, 6),
            new Point(9, 7),
            new Point(9, 8),
            new Point(9, 9),
            new Point(10, 7),
            new Point(10, 8),
            new Point(10, 9)));

    private final List<ShipBoardCell> stashedComponents;
    private int numStashed;

    public SecondShipBoard(FourColors color) {
        super(color);
        this.stashedComponents = List.of(new ShipBoardCell(), new ShipBoardCell());
        numStashed = 0;
    }

    @Override
    public Set<Point> getShipArea() {
        return shipArea;
    }

    //Stashing methods

    public void stashComponent() {
        stashedComponents.get(numStashed).setComponent(lastComponent);
        numStashed++;
        resetLastComponent();
        notifyObservers();
    }

    public void grabStashedComponent(int index) {
        Component grabbedComponent = stashedComponents.get(index).getComponent();
        for (int i = index + 1; i < stashedComponents.size(); i++) {
            stashedComponents.get(i-1).setComponent(stashedComponents.get(i).getComponent());
        }
        numStashed--;
        stashedComponents.get(numStashed).removeComponent();
        offerComponent(grabbedComponent);
    }

    @Override
    public List<ShipBoardCell> getStashedComponents() {
        return stashedComponents;
    }
}
