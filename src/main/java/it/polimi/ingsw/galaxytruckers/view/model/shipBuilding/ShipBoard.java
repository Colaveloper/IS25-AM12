package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class ShipBoard {

    protected final Map<Point, Component> componentMap;
    protected Component lastComponent;  // can be null
    protected Point lastPosition;  // can be null
    protected final FourColors color;

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

    public ShipBoard(FourColors color) { // (, Color color)
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

    }

    public abstract Set<Point> getShipArea();

    //CliComponentBank interaction methods

    public void setLastComponent(Component component) {
        lastComponent = component;
    }

    public void resetLastComponent() {
        this.lastComponent = null;
        this.lastPosition = null;
    }

    //Ship building methods


    public void setLastPosition(Point lastPosition) {
        if (lastPosition != null) {
            componentMap.remove(lastPosition);
        }
        componentMap.put(lastPosition, lastComponent);
        this.lastPosition = lastPosition;
    }

    public void stashComponent() {}

    public void grabStashedComponent(int index) {}

    public void removeComponent(Point position) {
        Component removedComponent = componentMap.remove(lastPosition);
        switch (removedComponent) {
            case Battery c -> batteries.remove(position);
            case Cabin c -> cabins.remove(position);
            case DoubleCannon c -> {
                cannons.remove(position);
                activatables.remove(position);
            }
            case Cannon c -> cannons.remove(position);
            case DoubleEngine c -> {
                engines.remove(position);
                activatables.remove(position);
            }
            case Engine c -> {
                engines.remove(position);
            }
            case CargoHold c -> {
                cargoHolds.remove(position);
            }
            case Shield c -> {
                shields.remove(position);
            }
            case Component c -> {}
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

    //Specific component methods

    public void placeGoods(Point position, GoodsType goods) {
        cargoHolds.get(position).addGoods(goods);
    }

    public void removeGoods(Point position, GoodsType goods) {
        cargoHolds.get(position).removeGoods(goods);
    }

    public void setBatteries(Point position, int amount) {
        batteries.get(position).setNumBatteries(amount);
    }

    public void setNumResidents(Point point, int numResidents) {
        cabins.get(point).setNumResidents(numResidents);
    }

    public void setCrewType(Point point, CrewType crewType) {
        cabins.get(point).setCrewType(crewType);
    }

    // Activatables methods

    public void setActivate(Point position, boolean activate) {
        Activatable activatable = activatables.get(position);
        if (activatable.isActive() != activate) {
            activatables.get(position).setActive(activate);
        }
    }

    public void deactivateAll() {
        for (Point p : activatables.keySet()) {
            if (activatables.get(p).isActive()) {
                setActivate(p, false);
            }
        }
    }
}
