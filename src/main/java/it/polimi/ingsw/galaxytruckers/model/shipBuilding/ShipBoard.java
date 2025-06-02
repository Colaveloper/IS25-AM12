package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class ShipBoard implements ComponentVisitor, ActivatableVisitor {

    protected final Map<Point, Component> componentMap;
    protected Component lastComponent;  // can be null
    protected Point lastPosition;  // can be null
    protected final GameColor color;

    protected int firePower;
    protected int enginePower;
    protected int numBatteries;
    protected int crewSize;
    protected int credits;
    protected int losses;
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

    public ShipBoard(GameColor color) { // (, Color color)
        this.componentMap = new HashMap<>();
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

        offerComponent(ComponentRegistry.getInstance().getStartingCabin(color));
        placeComponent(new Point(7,7),0);
        weldLastComponent();
    }

    protected abstract boolean containsPoint(Point point);

    public void gainCredits (int credits) {
        this.credits += credits;
    }

    //CliComponentBank interaction methods

    public void offerComponent(Component component) {
        weldLastComponent();
        lastComponent = component;
    }

    public Component rejectComponent() {
        Component rejectedComponent = lastComponent;
        lastComponent = null;
        lastPosition = null;
        return rejectedComponent;
    }

    //Ship building methods

    public void placeComponent(Point newPosition, int orientation) {
        if (lastComponent == null) {
            throw new IllegalStateException("There is no component to place");
        } else if (componentMap.containsKey(newPosition)) {
            throw new IllegalStateException("The position is already taken");
        } else if (!containsPoint(newPosition)) {
            throw new IllegalArgumentException("The position is outside the ship");
        }
        lastPosition = newPosition;
        lastComponent.setOrientation(orientation);
    }

    public void stashComponent() {}

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

    public void discardComponent(Point position) {
        removeComponent(position);
        losses++;
    }

    public void removeComponent(Point position) {
        lastPosition = position;
        componentMap.remove(lastPosition).removeFromVisitor(this);
        lastPosition = null;
    }

    public void finishBuilding() {
        try {
            weldLastComponent();
        } catch (IllegalStateException e) {
            rejectComponent();
        }
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

    public Map<GoodsType, Integer> getGoods() {
        return goods;
    }

    public int getGoodsValue() {
        return goods.keySet().stream()
                .mapToInt(g -> g.getValue()*goods.get(g))
                .sum();
    }

    public int getExposedConnectorsNumber() {
        int exposedConnectorsNumber = 0;
        for (Point point : componentMap.keySet()) {
            List<Point> neighbours = getNeighbours(point);
            for (int i = 0; i < neighbours.size(); i++) {
                if (!componentMap.containsKey(neighbours.get(i)) &&
                        componentMap.get(point).getConnectors().get(i) != Connector.NONE) {
                    exposedConnectorsNumber++;
                }
            }
        }
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

    public Map<Point, Cannon> getCannons() {
        return cannons;
    }

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

    public Optional<Point> getLastPosition() {
        return Optional.ofNullable(lastPosition);
    }

    public List<Component> getStashedComponents() {
        return null;
    }

    //CargoHold methods

    public void placeGoods(Point position, GoodsType goods, int amount) {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).addGoods(goods, amount);
        if (!this.goods.containsKey(goods)) {
            this.goods.put(goods, amount);
        } else {
            this.goods.put(goods, this.goods.get(goods) + amount);
        }
    }

    public void removeGoods(Point position, GoodsType goods, int amount) {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).removeGoods(goods, amount);
        this.goods.put(goods, this.goods.get(goods) - amount);
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

    public boolean activateComponent(Point position) {
        if (!activatables.containsKey(position)) {
            throw new IllegalStateException("There is no activatable for this position");
        }
        if (!activatables.get(position).isActive()) {
            activatables.get(position).activate(this);
            return true;
        }
        return false;
    }

    public void deactivateComponent(Point position) {
        if (!activatables.containsKey(position)) {
            throw new IllegalStateException("There is no activatable for this position");
        }
        if (activatables.get(position).isActive()) {
            activatables.get(position).deactivate(this);
        }
    }

    public void deactivateAll() {
        for (Point p : activatables.keySet()) {
            if (activatables.get(p).isActive()) {
                activatables.get(p).deactivate(this);
            }
        }
    }

    // Ship validity methods

    public boolean checkValidity() {
        for (Point point : componentMap.keySet()) {
            List<Point> neighbours = getNeighbours(point);
            for (int i = 0; i < neighbours.size(); i++) {
                if (componentMap.containsKey(neighbours.get(i))) {
                    Component currentComponent = componentMap.get(point);
                    Component neighbourComponent = componentMap.get(neighbours.get(i));
                    if (!currentComponent.getConnectors().get(i).matches(neighbourComponent.getConnectors().get((i+2)%4))) {
                        return false;
                    }
                }
            }
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
        for (int direction : shield.getDefensibleDirections()) {
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
        for (int direction : shield.getDefensibleDirections()) {
            this.shieldDirections[direction] -= 1;
        }
    }

    @Override
    public void add(Component component) {}

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
        this.activatables.put(this.lastPosition, shield);
    }

    @Override
    public void add(LifeSupport lifeSupport) {}

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
    public void remove(Component component) {}

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
        loseCrew(this.lastPosition, cabin.getNumResidents());
        this.cabins.remove(this.lastPosition);
    }

    @Override
    public void remove(Shield shield) {
        shield.deactivate(this);
        this.shields.remove(this.lastPosition);
        this.activatables.remove(this.lastPosition);
    }

    @Override
    public void remove(LifeSupport lifeSupport) {}

    @Override
    public void remove(CargoHold cargoHold) {
        Map<GoodsType, Integer> lostGoods = this.cargoHolds.get(lastPosition).getGoods();
        for (GoodsType goods: lostGoods.keySet()) {
            removeGoods(lastPosition, goods, lostGoods.get(goods));
        }
        this.cargoHolds.remove(this.lastPosition);
    }

    @Override
    public void remove(DoubleCannon doubleCannon) {
        doubleCannon.deactivate(this);
        this.cannons.remove(this.lastPosition);
        this.activatables.remove(this.lastPosition);
    }

    @Override
    public void remove(DoubleEngine doubleEngine) {
        doubleEngine.deactivate(this);
        this.engines.remove(this.lastPosition);
        this.activatables.remove(this.lastPosition);
    }

    public GameColor getColor() {
        return color;
    }
}
