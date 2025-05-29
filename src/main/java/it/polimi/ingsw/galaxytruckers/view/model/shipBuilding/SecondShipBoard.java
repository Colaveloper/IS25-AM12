package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;

import java.awt.*;
import java.util.List;
import java.util.*;

public class SecondShipBoard extends ShipBoard {
    private static final Set<Point> shipArea = new HashSet<>(List.of(
            new Point(4, 7),
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
            new Point(10, 9)
    ));

    private final ObservableList<Component> stashedComponents;
    private int numStashed;

    public SecondShipBoard(GameColor color) {
        super(color);
        this.stashedComponents = new ObservableList<>();
        numStashed = 0;
    }

    @Override
    public Set<Point> getShipArea() {
        return shipArea;
    }

    //Stashing methods

    public void stashComponent() {
        stashedComponents.add(lastComponent);
        numStashed++;
        resetLastComponent();
    }

    public void grabStashedComponent(int index) {
        Component grabbedComponent = stashedComponents.remove(index);
        offerComponent(grabbedComponent);
    }

    @Override
    public List<Component> getStashedComponents() {
        return stashedComponents.getUnmodifiableView();
    }
}
