package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;

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

    private final List<Component> stashedComponents;

    private final Set<CrewType> aliens = new HashSet<>();

    public SecondShipBoard(GameColor color) {
        super(color);
        this.stashedComponents = new ArrayList<>();
    }

    @Override
    public Set<Point> getShipArea() {
        return shipArea;
    }

    @Override
    public void finishBuilding() {
        super.finishBuilding();
        stashedComponents.clear();
    }

    @Override
    public void setStashedComponents(List<Component> stashedComponents) {
        this.stashedComponents.clear();
        this.stashedComponents.addAll(stashedComponents);
    }

    //Stashing methods

    public void stashComponent() {
        stashedComponents.add(lastComponent);
        if (lastPosition != null) componentMap.remove(lastPosition);
        resetLastComponent();
    }

    public void grabStashedComponent(int index) {
        Component grabbedComponent = stashedComponents.remove(index);
        offerComponent(grabbedComponent);
    }

    @Override
    public List<Component> getStashedComponents() {
        return stashedComponents;
    }

    //Aliens methods
    
    @Override
    public int initializeCabin(Point position, CrewType crewType) {
        int residents = super.initializeCabin(position, crewType);
        addAliens(crewType);
        return residents;
    }

    @Override
    public int getFirePower() {
        return (firePower > 0 && aliens.contains(CrewType.PURPLE))
                ? firePower + 4
                : firePower;
    }

    @Override
    public int getEnginePower() {
        return (enginePower > 0 && aliens.contains(CrewType.BROWN))
                ? enginePower + 2
                : enginePower;
    }

    @Override
    protected void add(Point point, Cabin cabin) {
        super.add(point, cabin);
        addAliens(cabin.getCrewType());
    }

    @Override
    protected void remove(Point point, Cabin cabin) {
        super.remove(point, cabin);
        removeAliens(cabin.getCrewType());
    }

    @Override
    public void loseCrew(Point position) {
        super.loseCrew(position);
        removeAliens(cabins.get(position).getCrewType());
    }

    private void addAliens(CrewType crewType) {
        switch (crewType) {
            case BROWN, PURPLE -> aliens.add(crewType);
            case HUMAN -> {}
        }
    }

    private void removeAliens(CrewType crewType) {
        aliens.remove(crewType);
    }
}
