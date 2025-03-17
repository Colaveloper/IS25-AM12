package it.polimi.ingsw.galaxytruckers.shipBuilding;

import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import jdk.jshell.spi.ExecutionControl;

import java.awt.Point;
import java.util.*;
import java.util.List;

public abstract class ShipBoard implements ComponentVisitor, ActivatableVisitor {

    protected final Map<Point, Component> componentMap;
    protected Component lastComponent;  // can be null
    protected Point lastPosition;  // can be null
    protected final ComponentBank componentBank;
    protected final Colors color;

    protected int firePower;
    protected int enginePower;
    protected int numBatteries;
    protected int crewSize;
    protected int credits;
    protected int losses;
    protected int exposedConnectorsNumber;
    protected final int[] shieldDirections;
    protected final Map<GoodsType, Integer> goods;
    // We might need this attribute to handle meteors and cannon hits better
    // protected List<Map<Integer, Integer>> cannonDirections;

    protected final Map<Point, Cannon> cannons;
    protected final Map<Point, Engine> engines;
    protected final Map<Point, Battery> batteries;
    protected final Map<Point, Shield> shields;
    protected final Map<Point, CargoHold>  cargoHolds;
    protected final Map<Point, Cabin> cabins;
    protected final Map<Point, Activatable> activatables;

    ShipBoard(ComponentBank componentBank, Colors color) { // (, Color color)
        this.componentMap = new HashMap<>();
        this.componentBank = componentBank;
        this.lastComponent = null;
        this.lastPosition = null;

        this.firePower = 0;
        this.enginePower = 0;
        this.numBatteries = 0;
        this.crewSize = 0;
        this.credits = 0;
        this.losses = 0;
        this.shieldDirections = new int[]{0,0,0,0};
        this.color = color;
        this.goods = new HashMap<>();
        this.cannons = new HashMap<>();
        this.engines = new HashMap<>();
        this.batteries = new HashMap<>();
        this.shields = new HashMap<>();
        this.cargoHolds = new HashMap<>();
        this.cabins = new HashMap<>();
        this.activatables = new HashMap<>();

    }

    public ShipBoard(Colors color) {
        this(ComponentBank.getInstance(), color);
    }

    protected abstract boolean containsPoint(Point point);

    public void gainCredits (int credits) {
        this.credits += credits;
    }

    //ComponentBank interaction methods

    public void requestRandComponent() {
        weldLastComponent();
        lastComponent = componentBank.getRanComponent();
    }

    public void requestComponent(int id) {
        weldLastComponent();
        lastComponent = componentBank.getComponent(id);
    }

    public void rejectComponent() {
        componentBank.addUncovered(lastComponent);
        lastComponent = null;
        lastPosition = null;
    }

    //Ship building methods

    public void placeComponent(Point newPosition) {
        if (lastComponent == null) {
            throw new IllegalStateException("There is no component to place");
        } else if (componentMap.containsKey(newPosition)) {
            throw new IllegalStateException("The position is already taken");
        } else if (!containsPoint(newPosition)) {
            throw new IllegalArgumentException("The position is outside the ship");
        }
        lastPosition = newPosition;
    }

    public void rotateComponent() {
        if (lastComponent == null) {
            throw new IllegalStateException("There is no component to rotate");
        }
        lastComponent.rotateLeft();
    }

    public void stashComponent() {};

    public void grabStashedComponent(int index) {}

    public void weldLastComponent() {
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

    //TODO: handle exposed connectors logic
    public void removeComponent(Point position) {
        lastPosition = position;
        componentMap.remove(lastPosition).removeFromVisitor(this);
        lastPosition = null;
    }

    public void incrementLosses(int amount) {
        losses += amount;
    }

    //Observers

    // Ship stats observers

    public int getFirePower() {
        return firePower;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public int getCrewSize() {
        return crewSize;
    }

    public int getCredits() { return credits; }

    public int getLosses() { return losses; }

    public int getGoodsValue() {
        return goods.keySet().stream()
                .mapToInt(g -> g.getValue()*goods.get(g))
                .sum();
    }

    public int getExposedConnectorsNumber() {
        return exposedConnectorsNumber;
    }

    public boolean[] getShieldDirections() {
        boolean[] res = new boolean[this.shieldDirections.length];
        for (int i = 0; i <  this.shieldDirections.length; i++) {
            res[i] = this.shieldDirections[i] > 0;
        }
        return res;
    }

    // Components Observers

    public Map<Point, Component> getComponentMap() {
        return componentMap;
    }

    public Map<Point, Cannon> getCannons() { return cannons; }

    public Map<Point, Engine> getEngines() {
        return engines;
    }

    public Map<Point, Battery> getBatteries() {
        return batteries;
    }

    public Map<Point, Shield> getShields() {
        return shields;
    }

    public Map<Point, Cabin> getCabins() {
        return cabins;
    }

    public Map<Point, CargoHold> getCargoHolds() {
        return cargoHolds;
    }

    public Map<Point, LifeSupport> getLifeSupports() {
        return null;
    }

    public Map<Point, Activatable> getActivatables() {
        return activatables;
    }

    public Optional<Component> getLastComponent() {
        return Optional.ofNullable(lastComponent);
    }

    public List<Component> getStashedComponents() {
        return null;
    }

    public Point getFirstComponentAt(int diceRoll, int direction) {
        return new java.awt.Point(5, 4);
        // TODO: make return with first component hit by the said projectile
        // TODO: choose how to handle different projectiles
    }

    //CargoHold methods

    //TODO: handle update of ship's goods
    public void placeGoods(Point position, GoodsType goods, int amount) {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).addGoods(goods, amount);
    }

    //TODO: handle update of ship's goods
    public void removeGoods(Point position, GoodsType goods, int amount) {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).removeGoods(goods, amount);
    }

    //Batteries methods

    public void useBatteries(Point position, int amount) {
        if (!batteries.containsKey(position)) {
            throw new IllegalStateException("There is no battery for this position");
        }
        batteries.get(position).useBatteries(amount);
        numBatteries -= amount;
    }

    //Cabin (and LifeSupport) methods

    public Set<CrewType> getCrewTypeOptions(Point position) {
        Set<CrewType> res = new HashSet<>();
        res.add(CrewType.HUMAN);
        return res;
    }

    public void initializeCabin(Point position, CrewType crewType) {
        if (!cabins.containsKey(position)) {
            throw new IllegalStateException("There is no cabin for this position");
        }
        //this block is probably not needed, controller can handle crewType
        if (!getCrewTypeOptions(position).contains(crewType)) {
            throw new IllegalStateException("This alien cannot to survive here");
        }
        cabins.get(position).initialize(crewType);
        crewSize += cabins.get(position).getNumResidents();
    }

    public void loseCrew(Point position, int amount) {
        if (!cabins.containsKey(position)) {
            throw new IllegalStateException("There is no cabin for this position");
        }
        cabins.get(position).loseResidents(amount);
        crewSize -= amount;
    }

    // Activatables methods

    public void activateComponent(Point position) {
        if (!activatables.containsKey(position)) {
            throw new IllegalStateException("There is no activatable for this position");
        }
        activatables.get(position).activate(this);
    }

    public void deactivateComponent(Point position) {
        if (!activatables.containsKey(position)) {
            throw new IllegalStateException("There is no activatable for this position");
        }
        activatables.get(position).deactivate(this);
    }

    // Ship validity methods

    //TODO: handle exposed connectors update
    //TODO: handle Components who are not connected
    public boolean checkValidity() {
        Set<Point> checkedPoints = new HashSet<>();
        List<Point> toCheck = new ArrayList<>();
        componentMap.keySet().stream().findAny().ifPresent(toCheck::add);
        while (!toCheck.isEmpty()) {
            Point current = toCheck.removeLast();
            List<Point> neighbours = getNeighbours(current);
            for (int i = 0; i < neighbours.size(); i++) {
                if (componentMap.containsKey(neighbours.get(i))) {
                    Component currentComponent = componentMap.get(current);
                    Component neighbourComponent = componentMap.get(neighbours.get(i));
                    if (!currentComponent.getConnectors().get(i).matches(neighbourComponent.getConnectors().get((i+2)%4))) {
                        return false;
                    }
                }
            }
            checkedPoints.add(current);
        }
        for (Point point : cannons.keySet()) {
            if (componentMap.containsKey(getNeighbours(point).get(cannons.get(point).getOrientation()))) {
                return false;
            }
        }
        for (Point point : engines.keySet()) {
            if (!engines.get(point).isValid() ||
                componentMap.containsKey(getNeighbours(point).get((engines.get(point).getOrientation()+2)%4))) {
                return false;
            }
        }
        return true;
    }

    public List<Set<Point>> getConnectedSets() {
        Set<Point> toVisit = new HashSet<>(componentMap.keySet());
        List<Set<Point>> res = new ArrayList<>();
        for (Point point : componentMap.keySet()) {
            if (toVisit.contains(point)) {
                res.add(new HashSet<>());
                List<Point> connectedPoints = new ArrayList<>();
                connectedPoints.add(point);
                toVisit.remove(point);
                while (!connectedPoints.isEmpty()) {
                    Point currentPoint = connectedPoints.removeLast();
                    res.getLast().add(currentPoint);
                    List<Point> neighbours = getNeighbours(currentPoint);
                    for (int i = 0; i < neighbours.size(); i++) {
                        if (componentMap.containsKey(neighbours.get(i)) &&
                                componentMap.get(currentPoint).getConnectors().get(i) != Connector.NONE &&
                                toVisit.contains(neighbours.get(i))) {
                            connectedPoints.add(neighbours.get(i));
                            toVisit.remove(neighbours.get(i));
                        }
                    }
                }
            }
        }
        return res;
    }

    //Utilities methods

    //  Not a very elegant solution, consider extending Point / defining utilities / creating an abstraction for orientation
    protected List<Point> getNeighbours(Point position) {
        List<Point> res = new ArrayList<>();
        res.add(new Point(position.x, position.y-1));
        res.add(new Point(position.x-1, position.y));
        res.add(new Point(position.x, position.y+1));
        res.add(new Point(position.x+1, position.y));
        return res;
    }
}
