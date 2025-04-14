package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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

    private final List<Component> stashedComponents;

    private final Set<CrewType> aliens;
    // We might need this attribute to handle meteors and cannon hits better
    // private List<Map<Integer, Integer>> cannonDirections;

    private final Map<Point, LifeSupport> lifeSupports;

    public SecondShipBoard(Colors color) {
        super(color);
        this.lifeSupports = new HashMap<>();
        this.stashedComponents = new ArrayList<>();
        this.aliens = new HashSet<>();
    }

    @Override
    protected boolean containsPoint(Point point) {
        return shipArea.contains(point);
    }

    //Stashing methods

    public void stashComponent() {
        if (lastComponent == null) {
            throw new IllegalStateException("There is no component to stash");
        }
        if (stashedComponents.size() >= 2) {
            throw new IllegalStateException("You can only have up to 2 stashed components");
        }
        stashedComponents.add(lastComponent);
        lastComponent = null;
        lastPosition = null;
    }

    public void grabStashedComponent(int index) {
        weldLastComponent();
        lastComponent = stashedComponents.remove(index);
    }

    @Override
    public void finishBuilding() {
        this.losses += stashedComponents.size();
        stashedComponents.clear();
    }

    //Observers

    // Ship stats observers

    @Override
    public int getFirePower() {
        return (firePower > 0 && aliens.contains(CrewType.PURPLE)) ? firePower+2 : firePower;
    }

    @Override
    public int getEnginePower() {
        return (enginePower > 0 && aliens.contains(CrewType.BROWN)) ? enginePower+2 : enginePower;
    }

    // Component observers

    @Override
    public Map<Point, LifeSupport> getLifeSupports() {
        return lifeSupports;
    }

    @Override
    public List<Component> getStashedComponents() {
        return stashedComponents;
    }

    //Cabin (and LifeSupport) methods

    @Override
    public Set<CrewType> getCrewTypeOptions(Point position) {
        Set<CrewType> res = new HashSet<>();
        if (!cabins.containsKey(position)) {
            throw new IllegalStateException("There is no cabin for this position");
        }
        res.add(CrewType.HUMAN);

        // aliens are not allowed on the starting cabin
        if(!position.equals(new Point(7, 7))) {
            List<Point> neighbours = getNeighbours(position);
            for (int i = 0; i < neighbours.size(); i++) {
                Point neighbour = neighbours.get(i);
                if (lifeSupports.containsKey(neighbour) &&
                        !aliens.contains(lifeSupports.get(neighbour).getAlienType()) &&
                        componentMap.get(position).getConnectors().get(i) != Connector.NONE) {
                    res.add(lifeSupports.get(neighbour).getAlienType());
                }
            }
        }
        return res;
    }

    public void initializeCabin(Point position, CrewType crewType) {
        super.initializeCabin(position, crewType);
        if (crewType != CrewType.HUMAN) {
            aliens.add(crewType);
        }
    }

    public void loseCrew(Point position, int amount) {
        super.loseCrew(position, amount);
        aliens.remove(cabins.get(position).getCrewType());
    }

    //Visitor pattern methods

    @Override
    public void add(LifeSupport lifeSupport) {
        this.lifeSupports.put(this.lastPosition, lifeSupport);
    }

    @Override
    public void remove(LifeSupport lifeSupport) {
        this.lifeSupports.remove(this.lastPosition);
        Set<Point> adjacentCabins = getNeighbours(lastPosition).stream()
                .filter(cabins.keySet()::contains)
                .filter(p -> cabins.get(p).getCrewType() != CrewType.HUMAN)
                .filter(p -> cabins.get(p).getNumResidents()>0)
                .collect(Collectors.toSet());

        for (Point point : adjacentCabins) {
            if (!getCrewTypeOptions(point).contains(cabins.get(point).getCrewType())) {
                this.crewSize--;
                this.aliens.remove(cabins.get(point).getCrewType());
            }
        }
    }
}
