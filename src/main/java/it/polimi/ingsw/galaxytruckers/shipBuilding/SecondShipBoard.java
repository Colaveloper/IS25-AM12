package it.polimi.ingsw.galaxytruckers.shipBuilding;

import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SecondShipBoard extends ShipBoard {
    private static final Set<Point> shipArea = new HashSet<Point>(List.of(new Point(4, 7),
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

    SecondShipBoard(ComponentBank componentBank, Colors color) {
        super(componentBank, color);
        this.lifeSupports = new HashMap<>();
        this.stashedComponents = new ArrayList<>();
        this.aliens = new HashSet<>();
    }

    public SecondShipBoard(Colors color) {
        this(ComponentBank.getInstance(), color);
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

    public void weldLastComponent() throws IllegalStateException {
        if (lastComponent != null) {
            if (lastPosition == null) {
                throw new IllegalStateException("You cannot weld last component without setting its position");
            }
            componentMap.put(lastPosition, lastComponent);
            lastComponent.addToVisitor(this);
            lastComponent = null;
            lastPosition = null;
        }
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

    public Map<Point, LifeSupport> getLifeSupports() {
        return lifeSupports;
    }

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
                if (lifeSupports.containsKey(neighbour) && componentMap.get(position).getConnectors().get(i) != Connector.NONE) {
                    res.add(lifeSupports.get(neighbour).getAlienType());
                }
            }
        }
        return res;
    }

    public void initializeCabin(Point position, CrewType crewType)  throws IllegalStateException {
        super.initializeCabin(position, crewType);
        if (crewType != CrewType.HUMAN) {
            aliens.add(crewType);
        }
    }

    public void loseCrew(Point position, int amount)  throws IllegalStateException, IllegalArgumentException {
        if (!cabins.containsKey(position)) {
            throw new IllegalStateException("There is no cabin for this position");
        }
        cabins.get(position).loseResidents(amount);
        crewSize -= amount;
        aliens.remove(cabins.get(position).getCrewType());
    }

    //Visitor pattern methods

    @Override
    public void activate(DoubleCannon doubleCannon) {
        this.firePower += doubleCannon.getFirePower();
    }

    @Override
    public void activate(DoubleEngine doubleEngine) {
        this.enginePower += doubleEngine.getEnginePower();
    }

    @Override
    public void activate(Shield shield) {
        for (int direction : shield.getProtectedDirections()) {
            this.shieldDirections[direction] += 1;
        }
    }

    @Override
    public void deactivate(DoubleCannon doubleCannon) {
        this.firePower -= doubleCannon.getFirePower();
    }

    @Override
    public void deactivate(DoubleEngine doubleEngine) {
        this.enginePower -= doubleEngine.getEnginePower();
    }

    @Override
    public void deactivate(Shield shield) {
        // REVIEW: change shield behaviour to simplify management
        for (int direction : shield.getProtectedDirections()) {
            this.shieldDirections[direction] -= 1;
        }
    }

    //TODO: consider removing this method
    @Override
    public void add(Component component) {
        return;
    }

    @Override
    public void add(Cannon cannon) {
        this.cannons.put(this.lastPosition, cannon);
        this.firePower += cannon.getFirePower();
    }

    @Override
    public void add(Engine engine) {
        this.engines.put(this.lastPosition, engine);
        this.enginePower += engine.getEnginePower();
    }

    @Override
    public void add(Battery battery) {
        this.batteries.put(this.lastPosition, battery);
        this.numBatteries += battery.getNumBatteries();
    }

    @Override
    public void add(Cabin cabin) {
        this.cabins.put(this.lastPosition, cabin);
    }

    @Override
    public void add(Shield shield) {
        this.shields.put(this.lastPosition, shield);
    }

    @Override
    public void add(LifeSupport lifeSupport) {
        this.lifeSupports.put(this.lastPosition, lifeSupport);
    }

    @Override
    public void add(CargoHold cargoHold) {
        this.cargoHolds.put(this.lastPosition, cargoHold);
    }

    @Override
    public void add(DoubleCannon doubleCannon) {
        this.cannons.put(this.lastPosition, doubleCannon);
        this.activatables.put(this.lastPosition, doubleCannon);
    }

    @Override
    public void add(DoubleEngine doubleEngine) {
        this.engines.put(this.lastPosition, doubleEngine);
        this.activatables.put(this.lastPosition, doubleEngine);
    }

    @Override
    //TODO: Consider removing this method
    public void remove(Component component) {
    }

    @Override
    public void remove(Cannon cannon) {
        this.cannons.remove(this.lastPosition);
        this.firePower -= cannon.getFirePower();
    }

    @Override
    public void remove(Engine engine) {
        this.engines.remove(this.lastPosition);
        this.enginePower -= engine.getEnginePower();
    }

    @Override
    public void remove(Battery battery) {
        this.batteries.remove(this.lastPosition);
        this.numBatteries -= battery.getNumBatteries();
    }

    @Override
    public void remove(Cabin cabin) {
        this.cabins.remove(this.lastPosition);
        this.crewSize -= cabin.getNumResidents();
        //TODO: handle alien loss
    }

    @Override
    public void remove(Shield shield) {
        this.shields.remove(this.lastPosition);
        //TODO: Handle shield deactivation/removal
    }

    @Override
    public void remove(LifeSupport lifeSupport) {
        this.lifeSupports.remove(this.lastPosition);
        //TODO: handle alien loss
    }

    @Override
    public void remove(CargoHold cargoHold) {
        this.cargoHolds.remove(this.lastPosition);
        //TODO: handle goods loss
    }

    @Override
    public void remove(DoubleCannon doubleCannon) {
        this.cannons.remove(this.lastPosition);
        this.activatables.remove(this.lastPosition);
        //TODO: handle cannon deactivation/removal
    }

    @Override
    public void remove(DoubleEngine doubleEngine) {
        this.engines.remove(this.lastPosition);
        this.activatables.remove(this.lastPosition);
        //TODO: handle engine deactivation/removal
    }
}
