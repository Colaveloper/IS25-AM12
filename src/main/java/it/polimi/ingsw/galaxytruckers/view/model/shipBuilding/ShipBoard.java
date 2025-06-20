package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;


import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.controller.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.IntStream;

public abstract class ShipBoard {

    protected final Map<Point, Component> componentMap;
    protected Component lastComponent;  // content can be null
    protected Point lastPosition;  // can be null
    protected final GameColor color;

    protected int firePower;
    protected int enginePower;
    protected int numBatteries;
    protected int crewSize;
    protected int credits;
    protected int losses;

    protected final Map<Point, Cannon> cannons;
    protected final Map<Point, Engine> engines;
    protected final Map<Point, Battery> batteries;
    protected final Map<Point, Shield> shields;
    protected final Map<Point, CargoHold>  cargoHolds;
    protected final Map<Point, Cabin> cabins;
    protected final Map<Point, Activatable> activatables;

    public ShipBoard(GameColor color) { // (, Color color)
        this.color = color;

        this.lastComponent = null;
        this.lastPosition = null;

        this.firePower = 0;
        this.enginePower = 0;
        this.numBatteries = 0;
        this.crewSize = 0;
        this.credits = 0;
        this.losses = 0;

        this.componentMap = new HashMap<>();
        this.cannons = new HashMap<>();
        this.engines = new HashMap<>();
        this.batteries = new HashMap<>();
        this.shields = new HashMap<>();
        this.cargoHolds = new HashMap<>();
        this.cabins = new HashMap<>();
        this.activatables = new HashMap<>();

        offerComponent(ComponentRegistry.getInstance().getStartingCabin(color));
        placeComponent(new Point(7,7),Direction.UP);
        weldLastComponent();
    }

    public abstract Set<Point> getShipArea();

    //region Setup methods
    public void setComponentMap(Map<Point, Component> componentMap) {
        this.componentMap.clear();
        for (Point point : componentMap.keySet()) {
            Component component = componentMap.get(point);
            this.componentMap.put(point, component);
            updateStats(point, component);
        }
    }

    public void setHand(Component lastComponent, Point lastPosition) {
        this.lastComponent = lastComponent;
        this.lastPosition = lastPosition;
        if (lastComponent != null && lastPosition != null) {
            componentMap.put(lastPosition, lastComponent);
        }
    }

    public void setStashedComponents(List<Component> stashedComponents) {
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }
    //endregion

    //CliComponentBank interaction methods

    protected void resetLastComponent() {
        this.lastComponent = null;
        this.lastPosition = null;
    }

    //Ship building methods

    //CliComponentBank interaction methods

    public void offerComponent(Component component) {
        weldLastComponent();
        lastComponent = component;
        lastPosition = null;
    }

    public void grabPlacedComponent() {
        componentMap.remove(lastPosition);
        lastPosition = null;
    }

    public Component rejectComponent() {
        if (lastPosition != null) {
            componentMap.remove(lastPosition);
        }
        Component rejectedComponent = lastComponent;
        lastComponent = null;
        lastPosition = null;
        return rejectedComponent;
    }

    //Ship building methods

    public void placeComponent(Point newPosition, Direction orientation) {
        if (lastPosition != null) {
            componentMap.remove(lastPosition);
        }
        lastPosition = newPosition;
        lastComponent.setOrientation(orientation);
        componentMap.put(newPosition, lastComponent);
    }

    public void stashComponent() {}

    public void grabStashedComponent(int index) {}

    public void weldLastComponent() {
        if (lastComponent != null) {
            updateStats(lastPosition, lastComponent);
            lastComponent = null;
            lastPosition = null;
        }
    }

    private void updateStats(Point position, Component component) {
        switch (component) {
            case Battery c -> {
                batteries.put(position, c);
                numBatteries += c.getNumBatteries();
            }
            case Cabin c -> {
                add(position,c);
            }
            case DoubleCannon c -> {
                cannons.put(position,c);
                activatables.put(position,c);
                firePower += c.getFirePower();
            }
            case Cannon c -> {
                cannons.put(position, c);
                firePower += c.getFirePower();
            }
            case DoubleEngine c -> {
                engines.put(position,c);
                activatables.put(position,c);
                enginePower += c.getEnginePower();
            }
            case Engine c -> {
                engines.put(position,c);
                enginePower += c.getEnginePower();
            }
            case CargoHold c -> {
                cargoHolds.put(position,c);
            }
            case Shield c -> {
                shields.put(position,c);
                activatables.put(position,c);
            }
            case LifeSupport _, Component _ -> {}
        }
    }

    public void finishBuilding() {
        if (lastComponent != null && lastPosition != null) {
            weldLastComponent();
        } else {
            lastComponent = null;
        }
    }

    public void removeComponent(Point position) {
        Component removedComponent = componentMap.remove(position);
        switch (removedComponent) {
            case Battery battery -> {
                batteries.remove(position);
                numBatteries -= battery.numBatteries;
            }
            case Cabin cabin -> {
                remove(position,cabin);
            }
            case DoubleCannon doubleCannon -> {
                cannons.remove(position);
                activatables.remove(position);
                firePower -= doubleCannon.getFirePower();
            }
            case Cannon cannon -> {
                cannons.remove(position);
                firePower -= cannon.getFirePower();
            }
            case DoubleEngine doubleEngine -> {
                engines.remove(position);
                activatables.remove(position);
                enginePower -= doubleEngine.getEnginePower();
            }
            case Engine engine -> {
                engines.remove(position);
                enginePower -= engine.getEnginePower();
            }
            case CargoHold _ -> {
                cargoHolds.remove(position);
            }
            case Shield _ -> {
                shields.remove(position);
                activatables.remove(position);
            }
            case LifeSupport _, Component _ -> {}
        }
    }

    public List<Point> removeShipPiece(List<Set<Point>> shipPieces, int pieceIndex) {
        List<Point> pointsToRemove = IntStream.range(0, shipPieces.size())
                .filter(i -> i != pieceIndex)
                .boxed()
                .flatMap(i -> shipPieces.get(i).stream())
                .toList();
        pointsToRemove.forEach(this::removeComponent);
        return pointsToRemove;
    }

    public void placeGoods(Point position, GoodsType goods) {
        cargoHolds.get(position).addGoods(goods);
    }

    public void removeGoods(Point position, GoodsType goods) {
        cargoHolds.get(position).removeGoods(goods);
    }

    //Batteries methods

    public void useBattery(Point position) {
        batteries.get(position).removeBattery();
        numBatteries--;
    }

    //Cabin (and LifeSupport) methods

    public int initializeCabin(Point position, CrewType crewType) {
        Cabin cabin = cabins.get(position);
        cabin.initialize(crewType);
        crewSize += cabin.getNumResidents();
        return cabin.getNumResidents();
    }

    public void loseCrew(Point position) {
        cabins.get(position).loseCrew();
    }

    // Activatables methods

    public void activateComponent(Point position) {
        Activatable component = activatables.get(position);
        component.setActive(true);
        switch (component) {
            case DoubleCannon doubleCannon -> firePower += doubleCannon.getFirePower();
            case DoubleEngine doubleEngine -> enginePower += doubleEngine.getEnginePower();
            case Shield _ -> {}
        }
    }

    public void deactivateComponent(Point position) {
        Activatable component = activatables.get(position);
        switch (component) {
            case DoubleCannon doubleCannon -> firePower -= doubleCannon.getFirePower();
            case DoubleEngine doubleEngine -> enginePower -= doubleEngine.getEnginePower();
            case Shield _ -> {}
        }
        component.setActive(false);
    }

    public void deactivateAll() {
        for (Point p : activatables.keySet()) {
            if (activatables.get(p).isActive()) deactivateComponent(p);
        }
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void incrementLosses(int amount) {
        this.losses += amount;
    }

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

    public Component getLastComponent() {
        return this.lastComponent;
    }

    public List<Component> getStashedComponents() {
        return null;
    }

    public Point getLastPosition() {
        return lastPosition;
    }

    public GameColor getColor() {
        return color;
    }

    //Stat update methods
    protected void add(Point point, Cabin cabin) {
        cabins.put(point, cabin);
        crewSize += cabin.getNumResidents();
    }

    protected void remove(Point point, Cabin cabin) {
        cabins.remove(point);
        crewSize -= cabin.getNumResidents();
    }
}
