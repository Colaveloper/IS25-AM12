package shipBuilding;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ShipBoard implements ComponentVisitor, ActivatableVisitor {

    private final Map<Point, Component> componentMap;
    private Component lastComponent;  // can be null
    private Point lastPosition;  // can be null
    private final ComponentBank componentBank;
    private final List<Component> stashedComponents;
    // private final Color color;

    public ShipBoard(int level, ComponentBank componentBank) { // (, Color color)
        this.componentMap = new HashMap<>();
        this.componentBank = componentBank;
        this.lastComponent = null;
        this.lastPosition = null;
        this.stashedComponents = new ArrayList<>();
        // this.color = color;

        //TODO: design a way to define which positions are available based on the level
    }

    public void requestRandComponent() {
        lastComponent = componentBank.getRanComponent();
    }

    public void requestComponent(int id) {
        lastComponent = componentBank.getComponent(id);
    }

    public void rejectComponent() {
        componentBank.addUncovered(lastComponent);
        lastComponent = null;
        lastPosition = null;
    }

    public void placeComponent(Point newPosition) {
        lastPosition = newPosition;
    }

    public void rotateComponent() {
        lastComponent.rotateLeft();
    }

    public void stashComponent() throws IllegalStateException {
        if (stashedComponents.size() >= 2) {
            throw new IllegalStateException("You can only have 2 stashed components");
        }
        stashedComponents.add(lastComponent);
    }

    public void getStashedComponent(int index) {
        if (index >= stashedComponents.size()) {
            throw new IllegalStateException("Index out of bounds");
        }
        lastComponent = stashedComponents.get(index);
    }

    public void weldLastComponent() {
        if (lastComponent != null) {
            if (lastPosition == null) {
                throw new IllegalStateException("You cannot weld last component without setting its position");
            }
            componentMap.put(lastPosition, lastComponent);
        }
    }

    private void removeComponent(Point position) {
        lastPosition = position;
        componentMap.remove(lastPosition).removeFromVisitor(this);
        lastPosition = null;
    }

    //TODO: implement methods to handle specific components

    @Override
    public void activate(DoubleCannon doubleCannon) {

    }

    @Override
    public void activate(DoubleEngine doubleEngine) {

    }

    @Override
    public void activate(Shield shield) {

    }

    @Override
    public void deactivate(DoubleCannon doubleCannon) {

    }

    @Override
    public void deactivate(DoubleEngine doubleEngine) {

    }

    @Override
    public void deactivate(Shield shield) {

    }

    @Override
    public void add(Component component) {

    }

    @Override
    public void add(Cannon cannon) {

    }

    @Override
    public void add(Engine engine) {

    }

    @Override
    public void add(Battery battery) {

    }

    @Override
    public void add(Cabin cabin) {

    }

    @Override
    public void add(Shield shield) {

    }

    @Override
    public void add(LifeSupport lifeSupport) {

    }

    @Override
    public void add(CargoHold cargoHold) {

    }

    @Override
    public void add(DoubleCannon doubleCannon) {

    }

    @Override
    public void add(DoubleEngine doubleEngine) {

    }

    @Override
    public void remove(Component component) {

    }

    @Override
    public void remove(Cannon cannon) {

    }

    @Override
    public void remove(Engine engine) {

    }

    @Override
    public void remove(Battery battery) {

    }

    @Override
    public void remove(Cabin cabin) {

    }

    @Override
    public void remove(Shield shield) {

    }

    @Override
    public void remove(LifeSupport lifeSupport) {

    }

    @Override
    public void remove(CargoHold cargoHold) {

    }

    @Override
    public void remove(DoubleCannon doubleCannon) {

    }

    @Override
    public void remove(DoubleEngine doubleEngine) {

    }
}
