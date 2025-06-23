package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ShipBoardStub extends ShipBoard {

    public ShipBoardStub() {
        super(GameColor.RED);
    }

    public ShipBoardStub(GameColor color) {
        super(color);
    }

    @Override
    public void setGameEventListener(GameEventListener gameEventListener) {
        super.setGameEventListener(gameEventListener);
    }

    @Override
    protected boolean containsPoint(Point point) {
        return false;
    }

    @Override
    public void gainCredits(int credits) {
        super.gainCredits(credits);
    }

    @Override
    public void removeAll(boolean discard) {
        super.removeAll(discard);
    }

    @Override
    public void keepShipPiece(List<Set<Point>> shipPieces, int pieceIndex, boolean discard) {
        super.keepShipPiece(shipPieces, pieceIndex, discard);
    }

    @Override
    public void addWeldedComponent(Component component, Point position, Direction direction) {
        super.addWeldedComponent(component, position, direction);
    }

    @Override
    public void offerComponent(Component component) {
        super.offerComponent(component);
    }

    @Override
    public Component rejectComponent() {
        return super.rejectComponent();
    }

    @Override
    public void placeComponent(Point newPosition, Direction orientation) {
        super.placeComponent(newPosition, orientation);
    }

    @Override
    public void stashComponent() {
        super.stashComponent();
    }

    @Override
    public void grabStashedComponent(int index) {
        super.grabStashedComponent(index);
    }

    @Override
    public void grabPlacedComponent() {
        super.grabPlacedComponent();
    }

    @Override
    public void weldLastComponent() {
        super.weldLastComponent();
    }

    @Override
    public void discardComponent(Point position) {
        super.discardComponent(position);
    }

    @Override
    public void removeComponent(Point position) {
        super.removeComponent(position);
    }

    @Override
    public void finishBuilding() {
        super.finishBuilding();
    }

    @Override
    public int getFirePower() {
        return super.getFirePower();
    }

    @Override
    public int getEnginePower() {
        return super.getEnginePower();
    }

    @Override
    public int getNumBatteries() {
        return super.getNumBatteries();
    }

    @Override
    public int getCrewSize() {
        return super.getCrewSize();
    }

    @Override
    public int getCredits() {
        return super.getCredits();
    }

    @Override
    public int getLosses() {
        return super.getLosses();
    }

    @Override
    public Map<GoodsType, Integer> getGoods() {
        return super.getGoods();
    }

    @Override
    public int getGoodsValue() {
        return super.getGoodsValue();
    }

    @Override
    public int getExposedConnectorsNumber() {
        return super.getExposedConnectorsNumber();
    }

    @Override
    public Set<Direction> getShieldDirections() {
        return super.getShieldDirections();
    }

    @Override
    public Point getCenter() {
        return super.getCenter();
    }

    @Override
    public Map<Point, Component> getComponentMap() {
        return super.getComponentMap();
    }

    @Override
    public Map<Point, Cannon> getCannons() {
        return super.getCannons();
    }

    @Override
    public Map<Point, Engine> getEngines() {
        return super.getEngines();
    }

    @Override
    public Map<Point, Battery> getBatteries() {
        return super.getBatteries();
    }

    @Override
    public Map<Point, Shield> getShields() {
        return super.getShields();
    }

    @Override
    public Map<Point, Cabin> getCabins() {
        return super.getCabins();
    }

    @Override
    public Map<Point, CargoHold> getCargoHolds() {
        return super.getCargoHolds();
    }

    @Override
    public Map<Point, LifeSupport> getLifeSupports() {
        return super.getLifeSupports();
    }

    @Override
    public Map<Point, Activatable> getActivatables() {
        return super.getActivatables();
    }

    @Override
    public Optional<Component> getLastComponent() {
        return super.getLastComponent();
    }

    @Override
    public Optional<Point> getLastPosition() {
        return super.getLastPosition();
    }

    @Override
    public List<Component> getStashedComponents() {
        return super.getStashedComponents();
    }

    @Override
    public void placeGoods(Point position, GoodsType goods, int amount) {
        super.placeGoods(position, goods, amount);
    }

    @Override
    public void removeGoods(Point position, GoodsType goods, int amount) {
        super.removeGoods(position, goods, amount);
    }

    @Override
    public void useBatteries(Point position) {
        super.useBatteries(position);
    }

    @Override
    public Set<CrewType> getCrewTypeOptions(Point position) {
        return super.getCrewTypeOptions(position);
    }

    @Override
    public void initializeCabin(Point position, CrewType crewType) {
        super.initializeCabin(position, crewType);
    }

    @Override
    public void loseCrew(Point position) {
        super.loseCrew(position);
    }

    @Override
    public boolean activateComponent(Point position) {
        return super.activateComponent(position);
    }

    @Override
    public void deactivateComponent(Point position) {
        super.deactivateComponent(position);
    }

    @Override
    public void deactivateAll() {
        super.deactivateAll();
    }

    @Override
    public boolean checkValidity() {
        return super.checkValidity();
    }

    @Override
    public List<Set<Point>> getConnectedSets() {
        return super.getConnectedSets();
    }

    @Override
    protected Map<Direction, Point> getNeighbours(Point position) {
        return super.getNeighbours(position);
    }

    @Override
    public void activate(DoubleCannon doubleCannon) {
        super.activate(doubleCannon);
    }

    @Override
    public void activate(DoubleEngine doubleEngine) {
        super.activate(doubleEngine);
    }

    @Override
    public void activate(Shield shield) {
        super.activate(shield);
    }

    @Override
    public void deactivate(DoubleCannon doubleCannon) {
        super.deactivate(doubleCannon);
    }

    @Override
    public void deactivate(DoubleEngine doubleEngine) {
        super.deactivate(doubleEngine);
    }

    @Override
    public void deactivate(Shield shield) {
        super.deactivate(shield);
    }

    @Override
    public void add(Cannon cannon) {
        super.add(cannon);
    }

    @Override
    public void add(Engine engine) {
        super.add(engine);
    }

    @Override
    public void add(Battery battery) {
        super.add(battery);
    }

    @Override
    public void add(Cabin cabin) {
        super.add(cabin);
    }

    @Override
    public void add(Shield shield) {
        super.add(shield);
    }

    @Override
    public void add(LifeSupport lifeSupport) {
        super.add(lifeSupport);
    }

    @Override
    public void add(CargoHold cargoHold) {
        super.add(cargoHold);
    }

    @Override
    public void add(DoubleCannon doubleCannon) {
        super.add(doubleCannon);
    }

    @Override
    public void add(DoubleEngine doubleEngine) {
        super.add(doubleEngine);
    }

    @Override
    public void remove(Cannon cannon) {
        super.remove(cannon);
    }

    @Override
    public void remove(Engine engine) {
        super.remove(engine);
    }

    @Override
    public void remove(Battery battery) {
        super.remove(battery);
    }

    @Override
    public void remove(Cabin cabin) {
        super.remove(cabin);
    }

    @Override
    public void remove(Shield shield) {
        super.remove(shield);
    }

    @Override
    public void remove(LifeSupport lifeSupport) {
        super.remove(lifeSupport);
    }

    @Override
    public void remove(CargoHold cargoHold) {
        super.remove(cargoHold);
    }

    @Override
    public void remove(DoubleCannon doubleCannon) {
        super.remove(doubleCannon);
    }

    @Override
    public void remove(DoubleEngine doubleEngine) {
        super.remove(doubleEngine);
    }
}