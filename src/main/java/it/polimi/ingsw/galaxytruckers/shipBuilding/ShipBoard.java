package it.polimi.ingsw.galaxytruckers.shipBuilding;

import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;

import java.awt.Point;
import java.util.*;
import java.util.List;

public class ShipBoard implements ComponentVisitor, ActivatableVisitor {

    private final Map<Point, Component> componentMap;
    private final List<Component> stashedComponents;
    private Component lastComponent;  // can be null
    private Point lastPosition;  // can be null
    private final ComponentBank componentBank;
    private final List<Point> shipArea;
    private final Colors color;

    private int firePower;
    private int enginePower;
    private int numBatteries;
    private int crewSize;
    private int credits;
    private int losses;
    private int exposedConnectorsNumber;
    private final int[] shieldDirections;
    private final Set<CrewType> aliens;
    private Map<GoodsType, Integer> goods;
    // We might need this attribute to handle meteors and cannon hits better
    // private List<Map<Integer, Integer>> cannonDirections;

    private Map<Point, Cannon> cannons;
    private Map<Point, Engine> engines;
    private Map<Point, Battery> batteries;
    private Map<Point, Shield> shields;
    private Map<Point, CargoHold>  cargoHolds;
    private Map<Point, Cabin> cabins;
    private Map<Point, LifeSupport> lifeSupports;
    private Map<Point, Activatable> activatables;

    public ShipBoard(ComponentBank componentBank, Level level, Colors color) { // (, Color color)
        this.componentMap = new HashMap<>();
        this.stashedComponents = new ArrayList<>();
        this.componentBank = componentBank;
        this.lastComponent = null;
        this.lastPosition = null;
        this.shipArea = level.getShipArea();

        this.firePower = 0;
        this.enginePower = 0;
        this.numBatteries = 0;
        this.crewSize = 0;
        this.credits = 0;
        this.losses = 0;
        this.shieldDirections = new int[4];
        for (int i = 0; i < 4; i++) {
            this.shieldDirections[i] = 0;
        }
        this.aliens = new HashSet<>();
        this.color = color;

    }

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

    public void placeComponent(Point newPosition) throws IllegalStateException, IllegalArgumentException {
        if (lastComponent == null) {
            throw new IllegalStateException("There is no component to place");
        } else if (componentMap.containsKey(newPosition)) {
            throw new IllegalStateException("The position is already taken");
        } else if (!shipArea.contains(newPosition)) {
            throw new IllegalArgumentException("The position is outside the ship");
        }
        lastPosition = newPosition;
    }

    public void rotateComponent() throws IllegalStateException {
        if (lastComponent == null) {
            throw new IllegalStateException("There is no component to rotate");
        }
        lastComponent.rotateLeft();
    }

    public void stashComponent() throws IllegalStateException {
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

    public void getStashedComponent(int index) throws IndexOutOfBoundsException {
        weldLastComponent();
        lastComponent = stashedComponents.remove(index);
    }

    //TODO: handle exposed connectors logic
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

    //TODO: handle exposed connectors logic
    public void removeComponent(Point position) {
        lastPosition = position;
        componentMap.remove(lastPosition).removeFromVisitor(this);
        lastPosition = null;
        losses += 1;
    }

    //Observers

    // Ship stats observers

    public int getFirePower() {
        return (firePower > 0 && aliens.contains(CrewType.PURPLE)) ? firePower+2 : firePower;
    }

    public int getEnginePower() {
        return (enginePower > 0 && aliens.contains(CrewType.BROWN)) ? enginePower+2 : enginePower;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public int getCrewSize() {
        return crewSize;
    }

    public int getCredits() { return credits; }

    public int getLosses() { return 0; }

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
        return lifeSupports;
    }

    public Map<Point, Activatable> getActivatables() {
        return activatables;
    }

    //CargoHold methods

    //TODO: handle update of ship's goods
    public void placeGoods(Point position, GoodsType goods, int amount) throws IllegalStateException, IllegalArgumentException {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).addGoods(goods, amount);
    }

    //TODO: handle update of ship's goods
    public void removeGoods(Point position, GoodsType goods, int amount) throws IllegalStateException, IllegalArgumentException {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).removeGoods(goods, amount);
    }

    //Batteries methods

    public void useBatteries(Point position, int amount) throws IllegalStateException, IllegalArgumentException {
        if (!batteries.containsKey(position)) {
            throw new IllegalStateException("There is no battery for this position");
        }
        batteries.get(position).useBatteries(amount);
        numBatteries -= amount;
    }

    //Cabin (and LifeSupport) methods

    public Set<CrewType> getCrewTypeOptions(Point position) throws IllegalStateException, IllegalArgumentException {
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

    public void initializeCabin(Point position, CrewType crewType) throws IllegalStateException {
        if (!cabins.containsKey(position)) {
            throw new IllegalStateException("There is no cabin for this position");
        }
        //this block is probably not needed, controller can handle crewType
        if (!getCrewTypeOptions(position).contains(crewType)) {
            throw new IllegalStateException("This alien cannot to survive here");
        }
        cabins.get(position).initialize(crewType);
        crewSize += cabins.get(position).getNumResidents();
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
    }

    // Activatables methods

    public void activateComponent(Point position) throws IllegalStateException {
        if (!activatables.containsKey(position)) {
            throw new IllegalStateException("There is no activatable for this position");
        }
        activatables.get(position).activate(this);
    }

    public void deactivateComponent(Point position) throws IllegalStateException {
        if (!activatables.containsKey(position)) {
            throw new IllegalStateException("There is no activatable for this position");
        }
        activatables.get(position).deactivate(this);
    }

    // Ship validity methods

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
    private List<Point> getNeighbours(Point position) {
        List<Point> res = new ArrayList<>();
        res.add(new Point(position.x, position.y-1));
        res.add(new Point(position.x-1, position.y));
        res.add(new Point(position.x, position.y+1));
        res.add(new Point(position.x+1, position.y));
        return res;
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
