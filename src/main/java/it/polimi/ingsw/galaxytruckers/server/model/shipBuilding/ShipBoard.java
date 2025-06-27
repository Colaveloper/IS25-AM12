package it.polimi.ingsw.galaxytruckers.server.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.server.model.GameEventListener;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Abstract class representing a ship board in the game.
 */
public abstract class ShipBoard {
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

    protected final Map<Point, Cannon> cannons;
    protected final Map<Point, Engine> engines;
    protected final Map<Point, Battery> batteries;
    protected final Map<Point, Shield> shields;
    protected final Map<Point, CargoHold>  cargoHolds;
    protected final Map<Point, Cabin> cabins;
    protected final Map<Point, Activatable> activatables;

    /**
     * Constructor for ShipBoard.
     *
     * @param color The color of the ship board.
     * @param eventListener The event listener to notify about ship events.
     */
    public ShipBoard(GameColor color, GameEventListener eventListener) {
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

    public void setup(Map<Point, Component> components, int credits, int losses) {
        componentMap.clear();
        for (Point point : components.keySet()) {
            Component component = components.get(point);
            addWeldedComponent(component, point, component.getOrientation());
        }
        this.credits = credits;
        this.losses = losses;
    }

    /**
     * Checks if the ship board contains a specific point by using static ship area
     * defined only in subclasses.
     *
     * @param point The point to check.
     * @return true if the ship board contains the point, false otherwise.
     */
    protected abstract boolean containsPoint(Point point);

    /**
     * Gains credits for the ship board.
     *
     * @param credits The number of credits to gain.
     */
    public void gainCredits (int credits) {
        this.credits += credits;
        eventListener.notifyGrabCreditsEvent(this, credits);
    }

    /**
     * Removes all components from the ship board, optionally discarding them.
     *
     * @param discard If true, components are discarded; otherwise, they are removed.
     */
    public void removeAll(boolean discard) {
        Set<Point> points = new HashSet<>(componentMap.keySet());
        points.remove(getCenter());
        for (Point p : points) {
            if (discard) discardComponent(p);
            else removeComponent(p);
        }
    }

    /**
     * Keeps a specific ship piece by removing all components not in the piece.
     *
     * @param shipPieces The list of ship pieces.
     * @param pieceIndex The index of the piece to keep.
     * @param discard If true, components are discarded; otherwise, they are removed.
     */
    public void keepShipPiece(List<Set<Point>> shipPieces, int pieceIndex, boolean discard) {
        Set<Point> componentsToRemove = getComponentMap().keySet();
        componentsToRemove.removeAll(shipPieces.get(pieceIndex));
        for (Point point : componentsToRemove) {
            removeComponent(point, discard);
        }
        eventListener.notifyShipPieceRemovalEvent(this, pieceIndex);
    }

    /**
     * Adds a component to the ship board at a specific position and orientation.
     *
     * @param component The component to add.
     * @param position The position where the component should be placed.
     * @param direction The direction/orientation of the component.
     */
    public void addWeldedComponent(Component component, Point position, Direction direction) {
        if (!componentMap.containsKey(position)) {
            lastPosition = position;
            component.setOrientation(direction);
            componentMap.put(position,component);
            add(component);
            lastPosition = null;
        }
    }

    //region interaction methods with the component bank

    /**
     * Offers a component to the player (represented by the {@link ShipBoard}),
     * so that it has exclusive control over it (i.e., he holds it in his hand).
     *
     * @param component The component to offer.
     */
    public void offerComponent(Component component) {
        weldLastComponent();
        lastComponent = component;
    }

    /**
     * Rejects the last offered component, returning it to the component bank.
     *
     * @return The rejected component.
     */
    public Component rejectComponent() {
        Component rejectedComponent = lastComponent;
        lastComponent = null;
        lastPosition = null;
        return rejectedComponent;
    }
    // endregion

    //region ship building methods

    /**
     * Places the last offered component at a specific position and orientation on the ship board.
     *
     * @param newPosition The position where the component should be placed.
     * @param orientation The orientation of the component.
     */
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

    /**
     * Stashes the last placed component, making it available for later retrieval, if
     * the specific ship board supports stashing.
     */
    public void stashComponent() {}

    /**
     * Grabs a stashed component by its index, making it available for placement, if
     * the specific ship board supports stashing.
     *
     * @param index The index of the stashed component to grab.
     */
    public void grabStashedComponent(int index) {}

    /**
     * Grabs the last placed component, making it available for placement.
     * This method is called when the player decides to place a component that was previously placed.
     */
    public void grabPlacedComponent() {
        if (lastComponent != null && lastPosition != null) {
            lastPosition = null;
            eventListener.notifyGrabPlacedComponentEvent(this);
        } else {
            throw new IllegalStateException("You don't have a placed component to grab");
        }
    }

    /**
     * Welds the last placed component to the ship board at its current position.
     * This method finalizes the placement of the component.
     */
    public void weldLastComponent() {
        if (lastComponent != null) {
            if (lastPosition == null) {
                throw new IllegalStateException("You cannot weld last component without setting its position");
            }
            componentMap.put(lastPosition, lastComponent);
            add(lastComponent);
            lastComponent = null;
            lastPosition = null;
        }
    }

    /**
     * Discards the last placed component, removing it from the ship board and notifying the event listener.
     * This method is called when the player decides to discard a component instead of placing it.
     *
     * @param position The position of the component to discard.
     */
    public void discardComponent(Point position) {
        removeComponent(position,true);
        eventListener.notifyRemoveComponentEvent(this,position);
    }

    /**
     * Removes a component from the ship board at a specific position and notifies the event listener.
     * This method is called when the player decides to remove a component from the ship board during ship building.
     *
     * @param position The position of the component to remove.
     */
    public void removeComponent(Point position) {
        removeComponent(position, false);
        eventListener.notifyRemoveComponentEvent(this,position);
    }

    /**
     * Removes a component from the ship board at a specific position, optionally discarding it.
     * This method is called when a game event (e.g., a projectile hit) requires the removal of a component.
     *
     * @param position The position of the component to remove.
     * @param discard If true, the component is discarded; otherwise, it is simply removed.
     */
    private void removeComponent(Point position, boolean discard) {
        lastPosition = position;
        switch (componentMap.get(position)) {
            case DoubleCannon doubleCannon -> remove(doubleCannon, position);
            case Cannon cannon -> remove(cannon, position);
            case DoubleEngine doubleEngine -> remove(doubleEngine, position);
            case Engine engine -> remove(engine, position);
            case Battery battery -> remove(battery, position);
            case Cabin cabin -> remove(cabin, position);
            case Shield shield -> remove(shield, position);
            case LifeSupport lifeSupport -> remove(lifeSupport, position);
            case CargoHold cargoHold -> remove(cargoHold, position);
            case Component _ -> {}
        }
        componentMap.remove(lastPosition);
        lastPosition = null;
        if (discard) losses++;
    }

    /**
     * Finalizes the ship building process by welding the last component if it exists.
     * This method is called when the player has finished placing components on the ship board.
     */
    public void finishBuilding() {
        if (lastComponent != null) {
            if (lastPosition != null) {
                weldLastComponent();
                lastPosition = null;
            }
            lastComponent = null;
        }
    }
    // endregion

    //region ship stats observers

    /**
     * @return the firepower of the ship board, to be intepreted as
     *      2 * the nominal firepower of the ship.
     */
    public int getFirePower() {
        return firePower;
    }

    /**
     * @return the engine power of the ship board
     */
    public int getEnginePower() {
        return enginePower;
    }

    /**
     * @return the number of batteries available on the ship board
     */
    public int getNumBatteries() {
        return numBatteries;
    }

    /**
     * @return the crew size of the ship board, representing the number of crew members
     */
    public int getCrewSize() {
        return crewSize;
    }

    /**
     * @return the number of credits gained by the ship board
     */
    public int getCredits() { return credits; }

    /**
     * @return the number of losses incurred by the ship board
     */
    public int getLosses() { return losses; }

    /**
     * @return the goods stored in the ship board's cargo holds.
     */
    public Map<GoodsType, Integer> getGoods() {
        return new HashMap<>(goods);
    }

    /**
     * Calculates the total value of goods stored in the ship board's cargo holds.
     *
     * @return the total value of goods, calculated as the sum of each good's value multiplied by its quantity.
     */
    public int getGoodsValue() {
        return goods.keySet().stream()
                .mapToInt(g -> g.getValue()*goods.get(g))
                .sum();
    }

    /**
     * Calculates the number of exposed connectors on the ship board.
     *
     * @return the number of exposed connectors, which are connectors that are not connected to any other component.
     */
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

    /**
     * @return the set of directions where the ship board has shields.
     * This set is used to determine which directions are defensible against attacks.
     */
    public Set<Direction> getShieldDirections() {
        return new HashSet<>(shieldDirections);
    }

    // endregion

    //region components observers

    /**
     * @return the center point of the ship board, which is a fixed point at (7, 7).
     */
    public Point getCenter() {
        return new Point(center);
    }

    /**
     * @return a map of all components on the ship board, where the key is the position and the value is the component.
     */
    public Map<Point, Component> getComponentMap() {
        return new HashMap<>(componentMap);
    }

    /**
     * @return a map of cannons on the ship board, where the key is the position and the value is the cannon.
     */
    public Map<Point, Cannon> getCannons() {
        return new HashMap<>(cannons);
    }

    /**
     * @return a map of engines on the ship board, where the key is the position and the value is the engine.
     */
    public Map<Point, Engine> getEngines() {
        return new HashMap<>(engines);
    }

    /**
     * @return a map of batteries on the ship board, where the key is the position and the value is the battery.
     */
    public Map<Point, Battery> getBatteries() {
        return new HashMap<>(batteries);
    }

    /**
     * @return a map of shields on the ship board, where the key is the position and the value is the shield.
     */
    public Map<Point, Shield> getShields() {
        return new HashMap<>(shields);
    }

    /**
     * @return a map of cabins on the ship board, where the key is the position and the value is the cabin.
     */
    public Map<Point, Cabin> getCabins() {
        return new HashMap<>(cabins);
    }

    /**
     * @return a map of cargo holds on the ship board, where the key is the position and the value is the cargo hold.
     */
    public Map<Point, CargoHold> getCargoHolds() {
        return new HashMap<>(cargoHolds);
    }

    /**
     * @return a map of life supports on the ship board, where the key is the position and the value is the life support.
     * Returns an empty map if the ship board does not support life supports.
     */
    public Map<Point, LifeSupport> getLifeSupports() {
        return new HashMap<>();
    }

    /**
     * @return a map of activatable components on the ship board, where the key is the position and the value is the activatable component.
     * This includes components like DoubleCannon, DoubleEngine, and Shield.
     */
    public Map<Point, Activatable> getActivatables() {
        return new HashMap<>(activatables);
    }

    /**
     * @return the component in hand for this ship board, if any.
     */
    public Optional<Component> getLastComponent() {
        return Optional.ofNullable(lastComponent);
    }

    /**
     * @return the position of the component in hand, if any.
     */
    public Optional<Point> getLastPosition() {
        return Optional.ofNullable(lastPosition);
    }

    /**
     * @return a list of stashed components.
     * Returns an empty list if the ship board does not support stashing components.
     */
    public List<Component> getStashedComponents() {
        return List.of();
    }
    // endregion

    //region cargo hold methods

    /**
     * Places goods of a specific type and amount in the cargo hold at a given position.
     *
     * @param position The position of the cargo hold.
     * @param goods The type of goods to place.
     * @param amount The amount of goods to place.
     */
    public void placeGoods(Point position, GoodsType goods, int amount) {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).addGoods(goods, amount);
        this.goods.merge(goods, amount, Integer::sum);
        eventListener.notifyGoodsUpdateEvent(this,position,goods,true);
    }

    /**
     * Removes goods of a specific type and amount from the cargo hold at a given position.
     *
     * @param position The position of the cargo hold.
     * @param goods    The type of goods to remove.
     */
    public void removeGoods(Point position, GoodsType goods) {
        if (!cargoHolds.containsKey(position)) {
            throw new IllegalStateException("There is no cargo hold for this position");
        }
        cargoHolds.get(position).removeGoods(goods, 1);
        this.goods.put(goods, this.goods.get(goods) - 1);
        eventListener.notifyGoodsUpdateEvent(this,position,goods,false);
    }
    // endregion

    //region batteries methods

    /**
     * Uses a battery at a specific position, reducing the number of batteries available.
     *
     * @param position The position of the battery to use.
     */
    public void useBatteries(Point position) {
        if (!batteries.containsKey(position)) {
            throw new IllegalStateException("There is no battery for this position");
        }
        batteries.get(position).useBatteries();
        numBatteries--;
        eventListener.notifyUseBatteryEvent(this,position);
    }
    // endregion

    //region cabin and life support methods
    /**
     * Returns the set of crew type options available for a cabin at a specific position.
     *
     * @param position The position of the cabin.
     * @return A set containing the available crew types.
     */
    public Set<CrewType> getCrewTypeOptions(Point position) {
        Set<CrewType> res = new HashSet<>();
        res.add(CrewType.HUMAN);
        return res;
    }

    /**
     * Initializes a cabin at a specific position with a crew type.
     * This method sets the crew type for the cabin and updates the crew size.
     *
     * @param position The position of the cabin to initialize.
     * @param crewType The crew type to assign to the cabin.
     * @throws IllegalStateException if there is no cabin at the specified position.
     */
    public void initializeCabin(Point position, CrewType crewType) {
        if (!cabins.containsKey(position)) {
            throw new IllegalStateException("There is no cabin for this position");
        }
        Cabin cabin = cabins.get(position);
        cabin.initialize(crewType);
        crewSize += cabin.getNumResidents();
        eventListener.notifyCabinInitializationEvent(this,position,crewType);
    }

    /**
     * Loses crew from a cabin at a specific position.
     * @param position The position of the cabin from which to lose crew.
     * @throws IllegalStateException if there is no cabin at the specified position.
     */
    public void loseCrew(Point position) {
        if (!cabins.containsKey(position)) {
            throw new IllegalStateException("There is no cabin for this position");
        }
        cabins.get(position).loseResidents();
        crewSize--;
        eventListener.notifyLoseCrewEvent(this,position);
    }
    // endregion

    // Activatables methods

    /**
     * Activates a component at a specific position if it is not already active.
     * This method checks if the activatable exists and activates it, notifying the event listener.
     *
     * @param position The position of the component to activate.
     * @return true if the component was successfully activated, false if it was already active.
     * @throws IllegalStateException if there is no activatable for the specified position.
     */
    public boolean activateComponent(Point position) {
        if (!activatables.containsKey(position)) {
            throw new IllegalStateException("There is no activatable for this position");
        }
        Activatable activatingComponent = activatables.get(position);
        if (!activatingComponent.isActive()) {
            activatingComponent.activate();
            switch (activatingComponent) {
                case DoubleCannon doubleCannon -> activate(doubleCannon);
                case DoubleEngine doubleEngine -> activate(doubleEngine);
                case Shield shield -> activate(shield);
            }
            eventListener.notifyActivateComponentEvent(this,position,true);
            return true;
        }
        return false;
    }

    /**
     * Deactivates a component at a specific position.
     * This method checks if the activatable exists and deactivates it, notifying the event listener.
     *
     * @param position The position of the component to deactivate.
     * @throws IllegalStateException if there is no activatable for the specified position,
     */
    public void deactivateComponent(Point position) {
        if (!activatables.containsKey(position)) {
            throw new IllegalStateException("There is no activatable for this position");
        }
        Activatable deactivatingComponent = activatables.get(position);
        if (deactivatingComponent.isActive()) {
            switch (deactivatingComponent) {
                case DoubleCannon doubleCannon -> deactivate(doubleCannon);
                case DoubleEngine doubleEngine -> deactivate(doubleEngine);
                case Shield shield -> deactivate(shield);
            }
            deactivatingComponent.deactivate();
        }
    }

    /**
     * Deactivates all active components on the ship board.
     * This method iterates through all activatables and deactivates them if they are active.
     */
    public void deactivateAll() {
        for (Point p : activatables.keySet()) {
            if (activatables.get(p).isActive()) {
                deactivateComponent(p);
            }
        }
    }

    // Ship validity methods

    /**
     * Checks the validity of the ship board.
     * This method verifies that all components are correctly connected and that cannons and engines are valid.
     *
     * @return true if the ship board is valid, false otherwise.
     */
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

    /**
     * Returns a list of connected sets of points on the ship board.
     * Each set represents a group of points that are connected through components.
     *
     * @return A list of sets of connected points.
     */
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

    // region utilities methods

    protected Map<Direction, Point> getNeighbours(Point position) {
        Map<Direction, Point> res = new HashMap<>();
        res.put(Direction.UP, new Point(position.x, position.y-1));
        res.put(Direction.LEFT, new Point(position.x-1, position.y));
        res.put(Direction.DOWN,new Point(position.x, position.y+1));
        res.put(Direction.RIGHT, new Point(position.x+1, position.y));
        return res;
    }
    // endregion

    // region private activation methods

    private void activate(DoubleCannon doubleCannon) {
        this.firePower += doubleCannon.getFirePower();
    }

    private void activate(DoubleEngine doubleEngine) {
        this.enginePower += doubleEngine.getEnginePower();
    }

    private void activate(Shield shield) {
        this.shieldDirections.addAll(shield.getDefensibleDirections());
    }
    // endregion

    // region private deactivation methods
    private void deactivate(DoubleCannon doubleCannon) {
        this.firePower -= doubleCannon.getFirePower();
    }

    private void deactivate(DoubleEngine doubleEngine) {
        this.enginePower -= doubleEngine.getEnginePower();
    }

    private void deactivate(Shield shield) {
        for (Direction direction : shield.getDefensibleDirections()) {
            this.shieldDirections.remove(direction);
        }
    }
    // endregion

    // region private add methods for components

    private void add(Component component) {
        switch (component) {
            case DoubleCannon doubleCannon -> add(doubleCannon, lastPosition);
            case Cannon cannon -> add(cannon, lastPosition);
            case DoubleEngine doubleEngine -> add(doubleEngine, lastPosition);
            case Engine engine -> add(engine, lastPosition);
            case Battery battery -> add(battery, lastPosition);
            case Cabin cabin -> add(cabin, lastPosition);
            case Shield shield -> add(shield, lastPosition);
            case CargoHold cargoHold -> add(cargoHold, lastPosition);
            case LifeSupport lifeSupport -> add(lifeSupport, lastPosition);
            case Component _ -> {}
        }
    }

    private void add(Cannon cannon, Point position) {
        this.cannons.put(position, cannon);
        this.firePower += cannon.getFirePower();
    }

    private void add(Engine engine, Point position) {
        this.engines.put(position, engine);
        this.enginePower += engine.getEnginePower();
    }

    private void add(Battery battery, Point position) {
        this.batteries.put(position, battery);
        this.numBatteries += battery.getNumBatteries();
    }

    private void add(Cabin cabin, Point position) {
        this.cabins.put(position, cabin);
        this.crewSize += cabin.getNumResidents();
    }

    private void add(Shield shield, Point position) {
        this.shields.put(position, shield);
        this.activatables.put(position, shield);
    }

    protected void add(LifeSupport lifeSupport, Point position) {}

    private void add(CargoHold cargoHold, Point position) {
        this.cargoHolds.put(position, cargoHold);
        for (GoodsType goodsType : cargoHold.getGoods().keySet()) {
            this.goods.merge(goodsType, cargoHold.getGoods().get(goodsType), Integer::sum);
        }
    }

    private void add(DoubleCannon doubleCannon, Point position) {
        add((Cannon) doubleCannon, position);
        this.activatables.put(position, doubleCannon);
    }

    private void add(DoubleEngine doubleEngine, Point position) {
        add((Engine) doubleEngine, position);
        this.activatables.put(position, doubleEngine);
    }

    //endregion

    // region remove methods for components

    private void remove(Cannon cannon, Point position) {
        this.cannons.remove(position);
        this.firePower -= cannon.getFirePower();
    }

    private void remove(Engine engine, Point position) {
        this.engines.remove(position);
        this.enginePower -= engine.getEnginePower();
    }

    private void remove(Battery battery, Point position) {
        this.batteries.remove(position);
        this.numBatteries -= battery.getNumBatteries();
    }

    protected void remove(Cabin cabin, Point position) {
        this.crewSize -= cabin.getNumResidents();
        this.cabins.remove(position);
    }

    private void remove(Shield shield, Point position) {
        deactivate(shield);
        this.shields.remove(position);
        this.activatables.remove(position);
    }

    protected void remove(LifeSupport lifeSupport, Point position) {}

    private void remove(CargoHold cargoHold, Point position) {
        Map<GoodsType, Integer> lostGoods = this.cargoHolds.get(position).getGoods();
        for (GoodsType goods: lostGoods.keySet()) {
            this.goods.put(goods, this.goods.get(goods) - lostGoods.get(goods));
            if (this.goods.get(goods) <= 0) this.goods.remove(goods);
        }
        this.cargoHolds.remove(position);
    }

    private void remove(DoubleCannon doubleCannon, Point position) {
        deactivate(doubleCannon);
        this.cannons.remove(position);
        this.activatables.remove(position);
    }

    private void remove(DoubleEngine doubleEngine, Point position) {
        deactivate(doubleEngine);
        this.engines.remove(position);
        this.activatables.remove(position);
    }
    //endregion
}
