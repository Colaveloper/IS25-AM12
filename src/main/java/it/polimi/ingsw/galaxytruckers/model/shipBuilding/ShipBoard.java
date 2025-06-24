package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.model.GameEventListener;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class ShipBoard implements ComponentVisitor, ActivatableVisitor {
    protected static final Point center = new Point(7,7);
    protected final GameEventListener eventListener;

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
    protected final Set<Direction> shieldDirections;
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

    public ShipBoard(GameColor color, GameEventListener eventListener) { // (, Color color)
        this.componentMap = new HashMap<>();
        this.lastComponent = null;
        this.lastPosition = null;

        this.firePower = 0;
        this.enginePower = 0;
        this.numBatteries = 0;
        this.crewSize = 0;
        this.credits = 0;
        this.losses = 0;
        this.shieldDirections = new HashSet<>();
        this.color = color;
        this.goods = new HashMap<>();
        this.cannons = new HashMap<>();
        this.engines = new HashMap<>();
        this.batteries = new HashMap<>();
        this.shields = new HashMap<>();
        this.cargoHolds = new HashMap<>();
        this.cabins = new HashMap<>();
        this.activatables = new HashMap<>();

        this.eventListener = eventListener;

        addWeldedComponent(ComponentRegistry.getInstance().getStartingCabin(color), center, Direction.UP);
    }

    protected abstract boolean containsPoint(Point point);

    public void gainCredits (int credits) {
        this.credits += credits;
        eventListener.notifyGrabCreditsEvent(this, credits);
    }

    public void removeAll(boolean discard) {
        Set<Point> points = new HashSet<>(componentMap.keySet());
        points.remove(getCenter());
        for (Point p : points) {
            if (discard) discardComponent(p);
            else removeComponent(p);
        }
    }

    public void keepShipPiece(List<Set<Point>> shipPieces, int pieceIndex, boolean discard) {
        Set<Point> componentsToRemove = getComponentMap().keySet();
        componentsToRemove.removeAll(shipPieces.get(pieceIndex));
        for (Point point : componentsToRemove) {
            removeComponent(point, discard);
        }
        eventListener.notifyShipPieceRemovalEvent(this, pieceIndex);
    }

    public void addWeldedComponent(Component component, Point position, Direction direction) {
        if (!componentMap.containsKey(position)) {
            lastPosition = position;
            component.setOrientation(direction);
            componentMap.put(position,component);
            component.addToVisitor(this, position);
            lastPosition = null;
        }
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

    public void placeComponent(Point newPosition, Direction orientation) {
        if (lastComponent == null) {
            throw new IllegalStateException("There is no component to place");
        } else if (componentMap.containsKey(newPosition)) {
            throw new IllegalStateException("The position is already taken");
        } else if (!containsPoint(newPosition)) {
            throw new IllegalArgumentException("The position is outside the ship");
        }
        lastPosition = newPosition;
        lastComponent.setOrientation(orientation);
        eventListener.notifyPlaceComponentEvent(this,orientation,newPosition);
    }

    public void stashComponent() {}

    public void grabStashedComponent(int index) {}

    public void grabPlacedComponent() {
        if (lastComponent != null && lastPosition != null) {
            lastPosition = null;
            eventListener.notifyGrabPlacedComponentEvent(this);
        } else {
            throw new IllegalStateException("You don't have a placed component to grab");
        }
    }

    public void weldLastComponent() {
        if (lastComponent != null) {
            if (lastPosition == null) {
                throw new IllegalStateException("You cannot weld last component without setting its position");
            }
            componentMap.put(lastPosition, lastComponent);
            lastComponent.addToVisitor(this, lastPosition);
            lastComponent = null;
            lastPosition = null;
        }
    }

    public void discardComponent(Point position) {
        removeComponent(position,true);
        eventListener.notifyRemoveComponentEvent(this,position);
    }

    public void removeComponent(Point position) {
        removeComponent(position, false);
        eventListener.notifyRemoveComponentEvent(this,position);
    }

    private void removeComponent(Point position, boolean discard) {
        lastPosition = position;
        componentMap.remove(lastPosition).removeFromVisitor(this, position);
        lastPosition = null;
        if (discard) losses++;
    }

    public void finishBuilding() {
        if (lastComponent != null) {
            if (lastPosition != null) {
                weldLastComponent();
                lastPosition = null;
            }
            lastComponent = null;
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
        return new HashMap<>(goods);
    }

    public int getGoodsValue() {
        return goods.keySet().stream()
                .mapToInt(g -> g.getValue()*goods.get(g))
                .sum();
    }

    public int getExposedConnectorsNumber() {
        int exposedConnectorsNumber = 0;
        for (Point point : componentMap.keySet()) {
            Map<Direction, Point> neighbours = getNeighbours(point);
            for (Direction direction : Direction.values()) {
                if (!componentMap.containsKey(neighbours.get(direction)) &&
                        componentMap.get(point).getConnectors().get(direction) != Connector.NONE) {
                    exposedConnectorsNumber++;
                }
            }
        }
        return exposedConnectorsNumber;
    }

    public Set<Direction> getShieldDirections() {
        return new HashSet<>(shieldDirections);
    }

    // Components Observers

    public Point getCenter() {
        return new Point(center);
    }

    public Map<Point, Component> getComponentMap() {
        return new HashMap<>(componentMap);
    }

    public Map<Point, Cannon> getCannons() {
        return new HashMap<>(cannons);
    }

    public Map<Point, Engine> getEngines() {
        return new HashMap<>(engines);
    }

    public Map<Point, Battery> getBatteries() {
        return new HashMap<>(batteries);
    }

    public Map<Point, Shield> getShields() {
        return new HashMap<>(shields);
    }

    public Map<Point, Cabin> getCabins() {
        return new HashMap<>(cabins);
    }

    public Map<Point, CargoHold> getCargoHolds() {
        return new HashMap<>(cargoHolds);
    }

    public Map<Point, LifeSupport> getLifeSupports() {
        return new HashMap<>();
    }

    public Map<Point, Activatable> getActivatables() {
        return new HashMap<>(activatables);
    }

    public Optional<Component> getLastComponent() {
        return Optional.ofNullable(lastComponent);
    }

    public Optional<Point> getLastPosition() {
        return Optional.ofNullable(lastPosition);
    }

    public List<Component> getStashedComponents() {
        return List.of();
    }

    //CargoHold methods

    public void placeGoods(Point position, GoodsType goods, int amount) {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).addGoods(goods, amount);
        this.goods.merge(goods, amount, Integer::sum);
        eventListener.notifyGoodsUpdateEvent(this,position,goods,true);
    }

    public void removeGoods(Point position, GoodsType goods, int amount) {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).removeGoods(goods, amount);
        this.goods.put(goods, this.goods.get(goods) - amount);
        eventListener.notifyGoodsUpdateEvent(this,position,goods,false);
    }

    //Batteries methods

    public void useBatteries(Point position) {
        if (!batteries.containsKey(position)) {
            throw new IllegalStateException("There is no battery for this position");
        }
        batteries.get(position).useBatteries();
        numBatteries--;
        eventListener.notifyUseBatteryEvent(this,position);
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
        Cabin cabin = cabins.get(position);
        cabin.initialize(crewType);
        crewSize += cabin.getNumResidents();
        eventListener.notifyCabinInitializationEvent(this,position,crewType);
    }

    public void loseCrew(Point position) {
        if (!cabins.containsKey(position)) {
            throw new IllegalStateException("There is no cabin for this position");
        }
        cabins.get(position).loseResidents();
        crewSize--;
        eventListener.notifyLoseCrewEvent(this,position);
    }

    // Activatables methods

    public boolean activateComponent(Point position) {
        if (!activatables.containsKey(position)) {
            throw new IllegalStateException("There is no activatable for this position");
        }
        if (!activatables.get(position).isActive()) {
            activatables.get(position).activate(this);
            eventListener.notifyActivateComponentEvent(this,position,true);
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
            Map<Direction, Point> neighbours = getNeighbours(point);
            for (Direction direction : Direction.values()) {
                if (componentMap.containsKey(neighbours.get(direction))) {
                    Component currentComponent = componentMap.get(point);
                    Component neighbourComponent = componentMap.get(neighbours.get(direction));
                    if (!currentComponent.getConnectors().get(direction).matches(neighbourComponent.getConnectors().get(direction.getOpposite()))) {
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
                componentMap.containsKey(getNeighbours(point).get((engines.get(point).getOrientation().getOpposite())))) {
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
                    Map<Direction, Point> neighbours = getNeighbours(currentPoint);
                    for (Direction direction : Direction.values()) {
                        if (componentMap.containsKey(neighbours.get(direction)) &&
                                componentMap.get(currentPoint).getConnectors().get(direction) != Connector.NONE &&
                                toVisit.contains(neighbours.get(direction))) {
                            connectedPoints.add(neighbours.get(direction));
                            toVisit.remove(neighbours.get(direction));
                        }
                    }
                }
            }
        }
        return res;
    }

    //Utilities methods

    protected Map<Direction, Point> getNeighbours(Point position) {
        Map<Direction, Point> res = new HashMap<>();
        res.put(Direction.UP, new Point(position.x, position.y-1));
        res.put(Direction.LEFT, new Point(position.x-1, position.y));
        res.put(Direction.DOWN,new Point(position.x, position.y+1));
        res.put(Direction.RIGHT, new Point(position.x+1, position.y));
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
        this.shieldDirections.addAll(shield.getDefensibleDirections());
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
        for (Direction direction : shield.getDefensibleDirections()) {
            this.shieldDirections.remove(direction);
        }
    }

    @Override
    public void add(Cannon cannon, Point position) {
        this.cannons.put(position, cannon);
        this.firePower += cannon.getFirePower();
    }

    @Override
    public void add(Engine engine, Point position) {
        this.engines.put(position, engine);
        this.enginePower += engine.getEnginePower();
    }

    @Override
    public void add(Battery battery, Point position) {
        this.batteries.put(position, battery);
        this.numBatteries += battery.getNumBatteries();
    }

    @Override
    public void add(Cabin cabin, Point position) {
        this.cabins.put(position, cabin);
    }

    @Override
    public void add(Shield shield, Point position) {
        this.shields.put(position, shield);
        this.activatables.put(position, shield);
    }

    @Override
    public void add(LifeSupport lifeSupport, Point position) {}

    @Override
    public void add(CargoHold cargoHold, Point position) {
        this.cargoHolds.put(position, cargoHold);
    }

    @Override
    public void add(DoubleCannon doubleCannon, Point position) {
        this.cannons.put(position, doubleCannon);
        this.activatables.put(position, doubleCannon);
    }

    @Override
    public void add(DoubleEngine doubleEngine, Point position) {
        this.engines.put(position, doubleEngine);
        this.activatables.put(position, doubleEngine);
    }

    @Override
    public void remove(Cannon cannon, Point position) {
        this.cannons.remove(position);
        this.firePower -= cannon.getFirePower();
    }

    @Override
    public void remove(Engine engine, Point position) {
        this.engines.remove(position);
        this.enginePower -= engine.getEnginePower();
    }

    @Override
    public void remove(Battery battery, Point position) {
        this.batteries.remove(position);
        this.numBatteries -= battery.getNumBatteries();
    }

    @Override
    public void remove(Cabin cabin, Point position) {
        this.crewSize -= cabin.getNumResidents();
        this.cabins.remove(position);
    }

    @Override
    public void remove(Shield shield, Point position) {
        shield.deactivate(this);
        this.shields.remove(position);
        this.activatables.remove(position);
    }

    @Override
    public void remove(LifeSupport lifeSupport, Point position) {}

    @Override
    public void remove(CargoHold cargoHold, Point position) {
        Map<GoodsType, Integer> lostGoods = this.cargoHolds.get(position).getGoods();
        for (GoodsType goods: lostGoods.keySet()) {
            this.goods.put(goods, this.goods.get(goods) - lostGoods.get(goods));
            if (this.goods.get(goods) <= 0) this.goods.remove(goods);
        }
        this.cargoHolds.remove(position);
    }

    @Override
    public void remove(DoubleCannon doubleCannon, Point position) {
        doubleCannon.deactivate(this);
        this.cannons.remove(position);
        this.activatables.remove(position);
    }

    @Override
    public void remove(DoubleEngine doubleEngine, Point position) {
        doubleEngine.deactivate(this);
        this.engines.remove(position);
        this.activatables.remove(position);
    }
}
