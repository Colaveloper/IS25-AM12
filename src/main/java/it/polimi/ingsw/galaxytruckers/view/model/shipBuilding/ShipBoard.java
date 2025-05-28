package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;


import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.observables.Invalidator;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableMap;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableGeneric;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.IntStream;

public abstract class ShipBoard implements Invalidator {

    protected final ObservableMap<Point, Component> componentMap;
    protected ObservableGeneric<Component> lastComponent;  // content can be null
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

    private final List<Listener> listeners = new ArrayList<>();

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

        this.componentMap = new ObservableMap<>();
        this.cannons = new HashMap<>();
        this.engines = new HashMap<>();
        this.batteries = new HashMap<>();
        this.shields = new HashMap<>();
        this.cargoHolds = new HashMap<>();
        this.cabins = new HashMap<>();
        this.activatables = new HashMap<>();
    }

    public abstract Set<Point> getShipArea();

    //CliComponentBank interaction methods

    protected void resetLastComponent() {
        this.lastComponent = null;
        this.lastPosition = null;
    }

    //Ship building methods

    //CliComponentBank interaction methods

    public void offerComponent(Component component) {
        weldLastComponent();
        lastComponent.setValue(component);
        lastPosition = null;
        notifyObservers();
    }

    public Component rejectComponent() {
        if (lastPosition != null) {
            componentMap.remove(lastPosition);
        }
        Component rejectedComponent = lastComponent.getValue();
        lastComponent = null;
        lastPosition = null;
        notifyObservers();
        return rejectedComponent;
    }

    //Ship building methods

    public void placeComponent(Point newPosition, int orientation) {
        if (lastPosition != null) {
            componentMap.remove(lastPosition);
        }
        lastPosition = newPosition;
        lastComponent.getValue().setOrientation(orientation);
        componentMap.put(newPosition, lastComponent.getValue());
        notifyObservers();
    }

    public void stashComponent() {}

    public void grabStashedComponent(int index) {}

    public void weldLastComponent() {
        switch (lastComponent.getValue()) {
            case Battery c -> batteries.put(lastPosition, c);
            case Cabin c -> cabins.put(lastPosition, c);
            case DoubleCannon c -> {
                cannons.put(lastPosition,c);
                activatables.put(lastPosition,c);
            }
            case Cannon c -> cannons.put(lastPosition,c);
            case DoubleEngine c -> {
                engines.put(lastPosition,c);
                activatables.put(lastPosition,c);
            }
            case Engine c -> {
                engines.put(lastPosition,c);
            }
            case CargoHold c -> {
                cargoHolds.put(lastPosition,c);
            }
            case Shield c -> {
                shields.put(lastPosition,c);
            }
            case Component c -> {}
        }
        lastComponent = null;
        lastPosition = null;
    }

    public void removeComponent(Point position) {
        Component removedComponent = componentMap.remove(position);
        switch (removedComponent) {
            case Battery _ -> batteries.remove(position);
            case Cabin _ -> cabins.remove(position);
            case DoubleCannon _ -> {
                cannons.remove(position);
                activatables.remove(position);
            }
            case Cannon _ -> cannons.remove(position);
            case DoubleEngine _ -> {
                engines.remove(position);
                activatables.remove(position);
            }
            case Engine _ -> {
                engines.remove(position);
            }
            case CargoHold _ -> {
                cargoHolds.remove(position);
            }
            case Shield _ -> {
                shields.remove(position);
            }
            case Component _ -> {
            }
        }
    }

    public void removeShipPiece(List<Set<Point>> shipPieces, int pieceIndex) {
        IntStream.range(0, shipPieces.size())
                .filter(i -> i != pieceIndex)
                .boxed()
                .flatMap(i -> shipPieces.get(i).stream())
                .forEach(this::removeComponent);
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
    }

    //Cabin (and LifeSupport) methods

    public void initializeCabin(Point position, CrewType crewType) {
        cabins.get(position).initialize(crewType);
    }

    public void loseCrew(Point position) {
        cabins.get(position).loseCrew();
    }

    // Activatables methods

    public void activateComponent(Point position) {
        activatables.get(position).setActive(true);
    }

    public void deactivateComponent(Point position) {
        activatables.get(position).setActive(false);
    }

    public void deactivateAll() {
        for (Point p : activatables.keySet()) {
            if (activatables.get(p).isActive()) {
                deactivateComponent(p);
            }
        }
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

    public ObservableMap<Point, Component> getComponentMap() {
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

    public ObservableGeneric<Component> getLastComponentProperty() {
        return lastComponent;
    }

    public Optional<Point> getLastPosition() {
        return Optional.ofNullable(lastPosition);
    }

    public GameColor getColor() {
        return color;
    }

    @Override
    public void addObserver(Listener o) {
        listeners.add(o);
    }

    @Override
    public void removeObserver(Listener o) {
        listeners.remove(o);
    }

    public void notifyObservers() {
        for (Listener o : listeners) {
            o.onNotified();
        }
    }

    public ObservableList<Component> getStashedComponentsProperty() {
        return null;
    };
}
