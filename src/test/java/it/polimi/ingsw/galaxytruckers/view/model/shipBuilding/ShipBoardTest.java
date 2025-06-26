package it.polimi.ingsw.galaxytruckers.view.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardTest {
    private TestShipBoard board = new TestShipBoard(GameColor.BLUE);

    @Test
    void getShipArea() {
        assertNotNull(board.getShipArea());
        assertFalse(board.getShipArea().isEmpty());
    }

    @Test
    void setComponentMap() {
        Map<Point, Component> map = new HashMap<>();
        map.put(new Point(1, 1), new Cabin(new HashMap<>(), 1));
        board.setComponentMap(map);
        assertEquals(1, board.getComponentMap().size());
    }

    @Test
    void setHand() {
        Cabin cabin = new Cabin(new HashMap<>(), 2);
        Point p = new Point(2, 2);
        board.setHand(cabin, p);
        assertEquals(cabin, board.getLastComponent());
        assertEquals(p, board.getLastPosition());
    }

    @Test
    void setHandNullComponentDoesNotPutInMap() {
        Point p = new Point(20, 20);
        int before = board.getComponentMap().size();
        board.setHand(null, p);
        assertNull(board.getLastComponent());
        assertEquals(p, board.getLastPosition());
        assertEquals(before, board.getComponentMap().size());
    }

    @Test
    void setHandNullPositionDoesNotPutInMap() {
        Cabin cabin = new Cabin(new HashMap<>(), 21);
        int before = board.getComponentMap().size();
        board.setHand(cabin, null);
        assertEquals(cabin, board.getLastComponent());
        assertNull(board.getLastPosition());
        assertEquals(before, board.getComponentMap().size());
    }

    @Test
    void setStashedComponents() {
        board.setStashedComponents(List.of());
    }

    @Test
    void setLosses() {
        board.setLosses(5);
        assertEquals(5, board.getLosses());
    }

    @Test
    void resetLastComponent() {
        Cabin cabin = new Cabin(new HashMap<>(), 3);
        Point p = new Point(3, 3);
        board.setHand(cabin, p);
        board.resetLastComponent();
        assertNull(board.getLastComponent());
        assertNull(board.getLastPosition());
    }

    @Test
    void resetLastComponentLastComponentNullDoesNotRemoveFromMap() {
        Point p = new Point(30, 30);
        board.lastPosition = p;
        board.lastComponent = null;
        int before = board.getComponentMap().size();
        board.resetLastComponent();
        assertNull(board.getLastComponent());
        assertNull(board.getLastPosition());
        assertEquals(before, board.getComponentMap().size());
    }

    @Test
    void resetLastComponentLastPositionNullDoesNotRemoveFromMap() {
        Cabin cabin = new Cabin(new HashMap<>(), 31);
        board.lastComponent = cabin;
        board.lastPosition = null;
        int before = board.getComponentMap().size();
        board.resetLastComponent();
        assertNull(board.getLastComponent());
        assertNull(board.getLastPosition());
        assertEquals(before, board.getComponentMap().size());
    }

    @Test
    void offerComponent() {
        Cabin cabin = new Cabin(new HashMap<>(), 4);
        board.offerComponent(cabin);
        assertEquals(cabin, board.getLastComponent());
    }

    @Test
    void offerComponentNullComponentPrintsMessageAndSetsLastComponentNull() {
        board.offerComponent(null);
        assertNull(board.getLastComponent());
        assertNull(board.getLastPosition());
    }

    @Test
    void grabPlacedComponent() {
        Cabin cabin = new Cabin(new HashMap<>(), 5);
        Point p = new Point(4, 4);
        board.setHand(cabin, p);
        board.grabPlacedComponent();
        assertNull(board.getLastPosition());
    }

    @Test
    void rejectComponent() {
        Cabin cabin = new Cabin(new HashMap<>(), 6);
        Point p = new Point(5, 5);
        board.setHand(cabin, p);
        Component rejected = board.rejectComponent();
        assertEquals(cabin, rejected);
        assertNull(board.getLastComponent());
    }

    @Test
    void rejectComponentLastPositionNullDoesNotRemoveFromMap() {
        Cabin cabin = new Cabin(new HashMap<>(), 100);
        board.lastComponent = cabin;
        board.lastPosition = null;
        int before = board.getComponentMap().size();
        Component rejected = board.rejectComponent();
        assertEquals(cabin, rejected);
        assertNull(board.getLastComponent());
        assertNull(board.getLastPosition());
        assertEquals(before, board.getComponentMap().size());
    }

    @Test
    void placeComponent() {
        Cabin cabin = new Cabin(new HashMap<>(), 7);
        board.offerComponent(cabin);
        Point p = new Point(6, 6);
        board.placeComponent(p, Direction.UP);
        assertEquals(cabin, board.getComponentMap().get(p));
    }

    @Test
    void finishBuilding() {
        Cabin cabin = new Cabin(new HashMap<>(), 8);
        Point p = new Point(7, 7);
        board.setHand(cabin, p);
        board.finishBuilding();
        assertNull(board.getLastComponent());
    }

    @Test
    void removeComponent() {
        Cabin cabin = new Cabin(new HashMap<>(), 9);
        Point p = new Point(8, 8);
        board.setHand(cabin, p);
        board.placeComponent(p, Direction.UP);
        board.weldLastComponent();
        board.removeComponent(p);
        assertNull(board.getComponentMap().get(p));
    }

    @Test
    void removeShipPiece() {
        Set<Point> piece1 = new HashSet<>(List.of(new Point(1, 1)));
        Set<Point> piece2 = new HashSet<>(List.of(new Point(2, 2)));
        List<Set<Point>> pieces = List.of(piece1, piece2);
        board.setComponentMap(Map.of(new Point(1, 1), new Cabin(new HashMap<>(), 10), new Point(2, 2), new Cabin(new HashMap<>(), 11)));
        List<Point> removed = board.removeShipPiece(pieces, 0);
        assertTrue(removed.contains(new Point(2, 2)));
    }

    @Test
    void placeGoodsAndRemoveGoods() {
        CargoHold hold = new CargoHold(new HashMap<>(), 12, true, 2);
        Point p = new Point(9, 9);
        board.getCargoHolds().put(p, hold);
        board.placeGoods(p, GoodsType.RED);
        assertEquals(1, board.getCargoOnShip());
        board.removeGoods(p, GoodsType.RED);
        assertEquals(0, board.getCargoOnShip());
    }

    @Test
    void useBattery() {
        Battery battery = new Battery(new HashMap<>(), 13, 2);
        Point p = new Point(10, 10);
        board.getBatteries().put(p, battery);
        int before = board.getNumBatteries();
        board.useBattery(p);
        assertEquals(before - 1, board.getNumBatteries());
    }

    @Test
    void initializeCabinAndLoseCrew() {
        Cabin cabin = new Cabin(new HashMap<>(), 14);
        Point p = new Point(11, 11);
        board.getCabins().put(p, cabin);
        int added = board.initializeCabin(p, CrewType.HUMAN);
        assertEquals(cabin.getNumResidents(), added);
        board.loseCrew(p);
        assertEquals(1, cabin.getNumResidents());
    }

    @Test
    void activateAndDeactivateComponent() {
        DoubleCannon cannon = new DoubleCannon(new HashMap<>(), 15);
        Point p = new Point(12, 12);
        board.getCannons().put(p, cannon);
        board.getActivatables().put(p, cannon);
        int before = board.getFirePower();
        board.activateComponent(p);
        assertTrue(cannon.isActive());
        assertEquals(before + cannon.getFirePower(), board.getFirePower());
        board.deactivateComponent(p);
        assertFalse(cannon.isActive());
        assertEquals(before, board.getFirePower());
    }

    @Test
    void deactivateAll() {
        DoubleEngine engine = new DoubleEngine(new HashMap<>(), 16);
        Point p = new Point(13, 13);
        board.getEngines().put(p, engine);
        board.getActivatables().put(p, engine);
        board.activateComponent(p);
        assertTrue(engine.isActive());
        board.deactivateAll();
        assertFalse(engine.isActive());
    }

    @Test
    void deactivateAll_ifFalse_noDeactivation() {
        // Use a real Shield (which is Activatable) and set it inactive
        Shield shield = new Shield(new HashMap<>(), 300);
        Point p = new Point(300, 300);
        board.getActivatables().put(p, shield);
        shield.setActive(false);
        board.deactivateAll();
        // Should remain inactive, and deactivateComponent should not change anything
        assertFalse(shield.isActive());
    }

    @Test
    void setCreditsAndIncrementLosses() {
        board.setCredits(42);
        assertEquals(42, board.getCredits());
        board.incrementLosses(3);
        assertEquals(3, board.getLosses());
    }

    @Test
    void getters() {
        assertNotNull(board.getComponentMap());
        assertNotNull(board.getCannons());
        assertNotNull(board.getEngines());
        assertNotNull(board.getBatteries());
        assertNotNull(board.getShields());
        assertNotNull(board.getCabins());
        assertNotNull(board.getCargoHolds());
        assertNull(board.getLifeSupports());
        assertNotNull(board.getActivatables());
        assertNotNull(board.getColor());
        assertNull(board.getStashedComponents());
    }

    @Test
    void stashComponentNoopCoverage() {
        board.stashComponent();
    }

    @Test
    void grabStashedComponentNoopCoverage() {
        board.grabStashedComponent(0);
    }

    @Test
    void updateStatsBattery() {
        Battery battery = new Battery(new HashMap<>(), 1, 3);
        Point p = new Point(1, 1);
        int before = board.getNumBatteries();
        board.getBatteries().clear();
        try {
            var m = ShipBoard.class.getDeclaredMethod("updateStats", Point.class, Component.class);
            m.setAccessible(true);
            m.invoke(board, p, battery);
        } catch (Exception e) { fail(e); }
        assertEquals(battery, board.getBatteries().get(p));
        assertEquals(before + 3, board.getNumBatteries());
    }

    @Test
    void updateStatsCabin() {
        Cabin cabin = new Cabin(new HashMap<>(), 2);
        Point p = new Point(2, 2);
        board.getCabins().clear();
        int before = board.getCrewSize();
        try {
            var m = ShipBoard.class.getDeclaredMethod("updateStats", Point.class, Component.class);
            m.setAccessible(true);
            m.invoke(board, p, cabin);
        } catch (Exception e) { fail(e); }
        assertEquals(cabin, board.getCabins().get(p));
    }

    @Test
    void updateStatsDoubleCannon() {
        DoubleCannon dc = new DoubleCannon(new HashMap<>(), 3);
        Point p = new Point(3, 3);
        board.getCannons().clear();
        board.getActivatables().clear();
        int before = board.getFirePower();
        try {
            var m = ShipBoard.class.getDeclaredMethod("updateStats", Point.class, Component.class);
            m.setAccessible(true);
            m.invoke(board, p, dc);
        } catch (Exception e) { fail(e); }
        assertEquals(dc, board.getCannons().get(p));
        assertEquals(dc, board.getActivatables().get(p));
        assertEquals(before + dc.getFirePower(), board.getFirePower());
    }

    @Test
    void updateStatsCannon() {
        Cannon cannon = new Cannon(new HashMap<>(), 4);
        Point p = new Point(4, 4);
        board.getCannons().clear();
        int before = board.getFirePower();
        try {
            var m = ShipBoard.class.getDeclaredMethod("updateStats", Point.class, Component.class);
            m.setAccessible(true);
            m.invoke(board, p, cannon);
        } catch (Exception e) { fail(e); }
        assertEquals(cannon, board.getCannons().get(p));
        assertEquals(before + cannon.getFirePower(), board.getFirePower());
    }

    @Test
    void updateStatsDoubleEngine() {
        DoubleEngine de = new DoubleEngine(new HashMap<>(), 5);
        Point p = new Point(5, 5);
        board.getEngines().clear();
        board.getActivatables().clear();
        int before = board.getEnginePower();
        try {
            var m = ShipBoard.class.getDeclaredMethod("updateStats", Point.class, Component.class);
            m.setAccessible(true);
            m.invoke(board, p, de);
        } catch (Exception e) { fail(e); }
        assertEquals(de, board.getEngines().get(p));
        assertEquals(de, board.getActivatables().get(p));
        assertEquals(before + de.getEnginePower(), board.getEnginePower());
    }

    @Test
    void updateStatsEngine() {
        Engine engine = new Engine(new HashMap<>(), 6);
        Point p = new Point(6, 6);
        board.getEngines().clear();
        int before = board.getEnginePower();
        try {
            var m = ShipBoard.class.getDeclaredMethod("updateStats", Point.class, Component.class);
            m.setAccessible(true);
            m.invoke(board, p, engine);
        } catch (Exception e) { fail(e); }
        assertEquals(engine, board.getEngines().get(p));
        assertEquals(before + engine.getEnginePower(), board.getEnginePower());
    }

    @Test
    void updateStatsCargoHold() {
        CargoHold hold = new CargoHold(new HashMap<>(), 7, true, 2);
        Point p = new Point(7, 7);
        board.getCargoHolds().clear();
        try {
            var m = ShipBoard.class.getDeclaredMethod("updateStats", Point.class, Component.class);
            m.setAccessible(true);
            m.invoke(board, p, hold);
        } catch (Exception e) { fail(e); }
        assertEquals(hold, board.getCargoHolds().get(p));
    }

    @Test
    void updateStatsShield() {
        Shield shield = new Shield(new HashMap<>(), 8);
        Point p = new Point(8, 8);
        board.getShields().clear();
        board.getActivatables().clear();
        try {
            var m = ShipBoard.class.getDeclaredMethod("updateStats", Point.class, Component.class);
            m.setAccessible(true);
            m.invoke(board, p, shield);
        } catch (Exception e) { fail(e); }
        assertEquals(shield, board.getShields().get(p));
        assertEquals(shield, board.getActivatables().get(p));
    }

    @Test
    void updateStatsLifeSupport() {
        LifeSupport ls = new LifeSupport(new HashMap<>(), 9, CrewType.HUMAN);
        Point p = new Point(9, 9);
        try {
            var m = ShipBoard.class.getDeclaredMethod("updateStats", Point.class, Component.class);
            m.setAccessible(true);
            m.invoke(board, p, ls);
        } catch (Exception e) { fail(e); }
        // No assertion needed, just coverage
    }

    @Test
    void removeComponentBattery() {
        Battery battery = new Battery(new HashMap<>(), 101, 2);
        Point p = new Point(101, 101);
        board.getComponentMap().put(p, battery);
        board.getBatteries().put(p, battery);
        board.numBatteries += battery.getNumBatteries();
        board.removeComponent(p);
        assertNull(board.getBatteries().get(p));
    }

    @Test
    void removeComponentCabin() {
        Cabin cabin = new Cabin(new HashMap<>(), 102);
        Point p = new Point(102, 102);
        board.getComponentMap().put(p, cabin);
        board.getCabins().put(p, cabin);
        board.removeComponent(p);
        assertNull(board.getCabins().get(p));
    }

    @Test
    void removeComponentDoubleCannon() {
        DoubleCannon dc = new DoubleCannon(new HashMap<>(), 103);
        Point p = new Point(103, 103);
        board.getComponentMap().put(p, dc);
        board.getCannons().put(p, dc);
        board.getActivatables().put(p, dc);
        board.firePower += dc.getFirePower();
        board.removeComponent(p);
        assertNull(board.getCannons().get(p));
        assertNull(board.getActivatables().get(p));
    }

    @Test
    void removeComponentCannon() {
        Cannon cannon = new Cannon(new HashMap<>(), 104);
        Point p = new Point(104, 104);
        board.getComponentMap().put(p, cannon);
        board.getCannons().put(p, cannon);
        board.firePower += cannon.getFirePower();
        board.removeComponent(p);
        assertNull(board.getCannons().get(p));
    }

    @Test
    void removeComponentDoubleEngine() {
        DoubleEngine de = new DoubleEngine(new HashMap<>(), 105);
        Point p = new Point(105, 105);
        board.getComponentMap().put(p, de);
        board.getEngines().put(p, de);
        board.getActivatables().put(p, de);
        board.enginePower += de.getEnginePower();
        board.removeComponent(p);
        assertNull(board.getEngines().get(p));
        assertNull(board.getActivatables().get(p));
    }

    @Test
    void removeComponentEngine() {
        Engine engine = new Engine(new HashMap<>(), 106);
        Point p = new Point(106, 106);
        board.getComponentMap().put(p, engine);
        board.getEngines().put(p, engine);
        board.enginePower += engine.getEnginePower();
        board.removeComponent(p);
        assertNull(board.getEngines().get(p));
    }

    @Test
    void removeComponentCargoHold() {
        CargoHold hold = new CargoHold(new HashMap<>(), 107, true, 2);
        Point p = new Point(107, 107);
        board.getComponentMap().put(p, hold);
        board.getCargoHolds().put(p, hold);
        hold.addGoods(GoodsType.RED);
        board.cargoOnShip += 1;
        board.removeComponent(p);
        assertNull(board.getCargoHolds().get(p));
    }

    @Test
    void removeComponentShield() {
        Shield shield = new Shield(new HashMap<>(), 108);
        Point p = new Point(108, 108);
        board.getComponentMap().put(p, shield);
        board.getShields().put(p, shield);
        board.getActivatables().put(p, shield);
        board.removeComponent(p);
        assertNull(board.getShields().get(p));
        assertNull(board.getActivatables().get(p));
    }

    @Test
    void removeComponentLifeSupport() {
        LifeSupport ls = new LifeSupport(new HashMap<>(), 109, CrewType.HUMAN);
        Point p = new Point(109, 109);
        board.getComponentMap().put(p, ls);
        board.removeComponent(p);
        // no assertion needed, just coverage
    }

    @Test
    void deactivateComponentShieldCase() {
        Shield shield = new Shield(new HashMap<>(), 200);
        Point p = new Point(200, 200);
        board.getActivatables().put(p, shield);
        shield.setActive(true);
        board.deactivateComponent(p);
        assertFalse(shield.isActive());
    }
}